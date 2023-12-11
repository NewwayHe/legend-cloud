/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.legendshop.activity.dao.ActivityCountDao;
import com.legendshop.activity.dto.*;
import com.legendshop.activity.enums.CouponProviderEnum;
import com.legendshop.activity.enums.MarketingTypeEnum;
import com.legendshop.activity.query.MarketingDataViewQuery;
import com.legendshop.activity.service.ActivityCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author legendshop
 */
@Service
public class ActivityCountServiceImpl implements ActivityCountService {

	@Autowired
	private ActivityCountDao activityCountDao;

	/**
	 * 平台端、商家端：表格图
	 */
	@Override
	public List<ActivityUsageCountDTO> getMasketingDataPage(MarketingDataViewQuery viewQuery) {

		// 查询天数
		long between = getBetween(viewQuery);

		//初始化返回值
		List<ActivityUsageCountDTO> activityList = new ArrayList<>();
		Map<Integer, List<ActivityUsageCountDTO>> activityMap = new HashMap<>(16);
		//8个类型
		List<MarketingTypeEnum> marketingTypeEnums = Arrays.asList(MarketingTypeEnum.values());
		marketingTypeEnums.stream().forEach(value -> {
			// 初始化单个活动对象
			ActivityUsageCountDTO activityUsageCountDTO = new ActivityUsageCountDTO();
			activityUsageCountDTO.setType(value.getType());
			activityList.add(activityUsageCountDTO);
		});
		//去除最后一个元素 (满减满折)
		activityList.remove(marketingTypeEnums.indexOf(MarketingTypeEnum.CASH_DISCOUNT));
		if (viewQuery.getShopId() != null) {
			activityList.remove(marketingTypeEnums.indexOf(MarketingTypeEnum.PLAT_COUPON));
		}

		//活动的使用次数
		List<ActivityUsageCountDTO> marketCountList = activityCountDao.marketingRewardUsageCount(viewQuery, MarketingTypeEnum.CASH.getType());
		List<ActivityUsageCountDTO> discountList = activityCountDao.marketingRewardUsageCount(viewQuery, MarketingTypeEnum.DISCOUNT.getType());
		List<ActivityUsageCountDTO> limitUsageCount = activityCountDao.marketingLimitUsageCount(viewQuery, true);

		List<ActivityUsageCountDTO> marketingCouponCount = activityCountDao.marketingCouponCount(viewQuery, CouponProviderEnum.SHOP, true);
		List<ActivityUsageCountDTO> platCouponCount = null;
		if (viewQuery.getShopId() == null) {
			platCouponCount = activityCountDao.marketingCouponCount(viewQuery, CouponProviderEnum.PLATFORM, true);
		}


		//校验结果
		activityMap = checkReturn(activityMap, marketCountList, discountList, null, limitUsageCount, marketingCouponCount, platCouponCount, true, viewQuery.getShopId());
		if (MapUtil.isEmpty(activityMap)) {
			return null;
		}
		//注入活动次数
		//遍历每个map
		activityMap.forEach((k, v) -> {
			activityList.stream().filter(e -> e.getType().equals(k)).findFirst().ifPresent(o -> {
				o.setUsage(v.get(0).getUsage() == null ? 0 : v.get(0).getUsage());
			});
		});

		//累计成交金额、成交数量、成交用户数量、新成交用户数、旧成交用户数、转化率

		List<ActivityUsageCountDTO> marketCountList2 = activityCountDao.marketingRewardDealCount(viewQuery, MarketingTypeEnum.CASH.getType());
		List<ActivityUsageCountDTO> discountList2 = activityCountDao.marketingRewardDealCount(viewQuery, MarketingTypeEnum.DISCOUNT.getType());
		List<ActivityUsageCountDTO> limitUsageCount2 = activityCountDao.marketingLimitDealCount(viewQuery, true);

		List<ActivityUsageCountDTO> marketingCouponCount2 = activityCountDao.marketingCouponDealCount(viewQuery, CouponProviderEnum.SHOP, true);
		List<ActivityUsageCountDTO> platCouponCount2 = null;
		if (viewQuery.getShopId() == null) {
			platCouponCount2 = activityCountDao.marketingCouponDealCount(viewQuery, CouponProviderEnum.PLATFORM, true);
		}

		//校验结果
		activityMap = checkReturn(activityMap, marketCountList2, discountList2, null, limitUsageCount2, marketingCouponCount2, platCouponCount2, true, viewQuery.getShopId());

		//注入活动其他信息
		activityMap.forEach((k, v) -> {
			activityList.stream().filter(e -> e.getType().equals(k)).findFirst().ifPresent(o -> {
				ActivityUsageCountDTO activity = v.get(0);
				o.setAmount(activity.getAmount() == null ? BigDecimal.ZERO : activity.getAmount());
				o.setCount(activity.getCount() == null ? 0 : activity.getCount());
				o.setDealUserNum(activity.getDealUserNum() == null ? 0 : activity.getDealUserNum());
				o.setOldUserNum(activity.getOldUserNum() == null ? 0 : activity.getOldUserNum());
				o.setNewUserNum(activity.getNewUserNum() == null ? 0 : activity.getNewUserNum());
				o.setTotalView(activity.getTotalView() == null ? 0 : activity.getTotalView());
				o.setTotalViewPeople(activity.getTotalViewPeople() == null ? 0 : activity.getTotalViewPeople());
				o.setInversionRate(activity.getInversionRate() == null ? 0D : activity.getInversionRate());
			});
		});

		if (ObjectUtil.isEmpty(viewQuery.getOrder())) {
			return activityList;
		}
		//排序返回
		return activityList.stream().sorted((a1, a2) -> {
			Class listClass = a1.getClass();
			try {
				//通过反射获取get方法，获取指定字段排序
				Method m = listClass.getMethod("get" + StrUtil.upperFirst(viewQuery.getProp()));
				int i = new BigDecimal(m.invoke(a1).toString()).compareTo(new BigDecimal(m.invoke(a2).toString()));
				return viewQuery.getOrder() ? i : -i;
			} catch (Exception e) {
				e.printStackTrace();
				return 1;
			}
		}).collect(Collectors.toList());
	}


	/**
	 * 平台端：新增使用次数
	 */
	@Override
	public List<MarketingDataViewLineDTO> getMasketingViewLine(MarketingDataViewQuery viewQuery) {

		if (StrUtil.isNotBlank(viewQuery.getProp()) || ObjectUtil.isNotEmpty(viewQuery.getOrder())) {
			return null;
		}

		// 查询天数
		long between = getBetween(viewQuery);

		// 初始化单个活动对象
		List<ActivityUsageCountDTO> list = new ArrayList<>();
		Map<Integer, List<ActivityUsageCountDTO>> activityMap = new HashMap<>(16);
		for (int i = 0; i < between; i++) {
			list.add(new ActivityUsageCountDTO(DateUtil.offsetDay(viewQuery.getStartDate(), i), 0, null));
		}
		// 查询时间段内的满减满折发布次数
		List<ActivityUsageCountDTO> countList = activityCountDao.marketingRewardUsageCount(viewQuery, null);
		//查询时间段内的限时折扣发布次数
		List<ActivityUsageCountDTO> limitUsageCount = activityCountDao.marketingLimitUsageCount(viewQuery, false);
		//查询时间段内的优惠卷、红包发布数
		List<ActivityUsageCountDTO> marketingCouponCount = activityCountDao.marketingCouponCount(viewQuery, CouponProviderEnum.SHOP, false);
		List<ActivityUsageCountDTO> platCouponCount = null;
		if (viewQuery.getShopId() == null) {
			platCouponCount = activityCountDao.marketingCouponCount(viewQuery, CouponProviderEnum.PLATFORM, false);
		}

		//校验结果
		Map<Integer, List<ActivityUsageCountDTO>> listMap = checkReturn(activityMap, null, null, countList, limitUsageCount, marketingCouponCount, platCouponCount, false, viewQuery.getShopId());
		if (MapUtil.isEmpty(listMap)) {
			return null;
		}

		//组装返回值
		List<MarketingDataViewLineDTO> marketingDataViewLineDTOList = new ArrayList<>();
		for (int i = 0; i < between; i++) {
			marketingDataViewLineDTOList.add(MarketingDataViewLineDTO.builder()
					.time(DateUtil.offsetDay(viewQuery.getStartDate(), i))
					.cashNum(0)
					.discountNum(0)
					.couponNum(0)
					//7个营销活动注入时间和初始次数
					.platNum(0).build());
		}

		// 将发布次数合并到初始list               1  ~  7天 查到的只有2号和5号有
		//首先获取第一个营销活动
		for (Map.Entry<Integer, List<ActivityUsageCountDTO>> entry : listMap.entrySet()) {
			//一个营销活动遍历每一天有的东西(从数据库获取)
			for (ActivityUsageCountDTO activityUsageCountDTO : entry.getValue()) {
				//一个营销活动注入对应那天的存在的东西
				list.stream().filter(e -> e.getDate().equals(activityUsageCountDTO.getDate())).findFirst().ifPresent(o -> {
					o.setUsage(activityUsageCountDTO.getUsage());
					o.setType(entry.getKey());
				});
			}
			//一个营销活动在哪一天有值 这里存完了
			//开始遍历每一天的重新组装
			for (MarketingDataViewLineDTO marketingDataViewLineDTO : marketingDataViewLineDTOList) {
				//活动标识
				Integer marketingType = entry.getKey();
				list.stream().filter(e -> {
					return e.getDate().equals(marketingDataViewLineDTO.getTime()) && e.getType() != null && e.getType().equals(marketingType);
				}).findFirst().ifPresent(o -> {
					buildResult(marketingDataViewLineDTO, null, marketingType, o);
				});
			}
		}
		return marketingDataViewLineDTOList;
	}


	/**
	 * 平台端：新增成交金额
	 */
	@Override
	public List<MarketingDataDealLineDTO> getMasketingDealLine(MarketingDataViewQuery viewQuery) {

		if (StrUtil.isNotBlank(viewQuery.getProp()) || ObjectUtil.isNotEmpty(viewQuery.getOrder())) {
			return null;
		}
		//查询天数
		long between = getBetween(viewQuery);

		// 初始化单个对象
		List<ActivityUsageCountDTO> list = new ArrayList<>();
		Map<Integer, List<ActivityUsageCountDTO>> activityMap = new HashMap<>(16);
		for (int i = 0; i < between; i++) {
			list.add(new ActivityUsageCountDTO(DateUtil.offsetDay(viewQuery.getStartDate(), i), new BigDecimal("0.00"), null));
		}
		List<ActivityUsageCountDTO> countList = activityCountDao.marketingRewardDealCount(viewQuery, null);
		List<ActivityUsageCountDTO> limitUsageCount = activityCountDao.marketingLimitDealCount(viewQuery, false);
		//查询时间段内的优惠卷、红包成交数
		List<ActivityUsageCountDTO> marketingCouponCount = activityCountDao.marketingCouponDealCount(viewQuery, CouponProviderEnum.SHOP, false);
		List<ActivityUsageCountDTO> platCouponCount = null;
		if (viewQuery.getShopId() == null) {
			platCouponCount = activityCountDao.marketingCouponDealCount(viewQuery, CouponProviderEnum.PLATFORM, false);
		}


		//校验结果
		Map<Integer, List<ActivityUsageCountDTO>> activityResult = checkReturn(activityMap, null, null, countList, limitUsageCount, marketingCouponCount, platCouponCount, false, viewQuery.getShopId());
		if (activityResult.isEmpty()) {
			return null;
		}

		//组装返回值
		List<MarketingDataDealLineDTO> marketingDataDealLineDTOList = new ArrayList<>();
		for (int i = 0; i < between; i++) {
			marketingDataDealLineDTOList.add(MarketingDataDealLineDTO.builder()
					.time(DateUtil.offsetDay(viewQuery.getStartDate(), i))
					.cashAmount(BigDecimal.ZERO)
					.discountAmount(BigDecimal.ZERO)
					.platAmount(viewQuery.getShopId() == null ? BigDecimal.ZERO : null)
					.build());//7个营销活动注入时间
		}

		// 将发布次数合并到初始list               1  ~  7天 查到的若只有2号和5号有
		//首先获取第一个营销活动
		for (Map.Entry<Integer, List<ActivityUsageCountDTO>> entry : activityResult.entrySet()) {
			//一个营销活动遍历每一天有的东西(从数据库获取)
			for (ActivityUsageCountDTO activityUsageCountDTO : entry.getValue()) {
				//一个营销活动注入对应那天的存在的东西
				list.stream().filter(e -> e.getDate().equals(activityUsageCountDTO.getDate())).findFirst().ifPresent(o -> {
					o.setAmount(activityUsageCountDTO.getAmount());
					o.setType(entry.getKey());
				});
			}
			//一个营销活动在哪一天有存完了
			//遍历每一天的
			for (MarketingDataDealLineDTO marketingDataDealLineDTO : marketingDataDealLineDTOList) {
				//活动标识
				Integer marketingType = entry.getKey();
				list.stream().filter(e -> {
					return e.getDate().equals(marketingDataDealLineDTO.getTime()) && e.getType() != null && e.getType().equals(marketingType);
				}).findFirst().ifPresent(o -> {
					buildResult(null, marketingDataDealLineDTO, marketingType, o);
				});
			}
		}
		return marketingDataDealLineDTOList;

	}


	/**
	 * 商家端：新增活动发布数
	 */
	@Override
	public List<ActivityShopUsageDTO> getMasketingTotalCount(MarketingDataViewQuery viewQuery) {

		// 查询天数
		long between = getBetween(viewQuery);
		//返回值
		List<ActivityShopUsageDTO> activityList = new ArrayList<>();
		Map<Integer, List<ActivityUsageCountDTO>> activityMap = getIntegerListMap(activityList);

		//活动的使用次数
		List<ActivityUsageCountDTO> marketCountList = activityCountDao.marketingRewardUsageCount(viewQuery, MarketingTypeEnum.CASH.getType());
		List<ActivityUsageCountDTO> discountList = activityCountDao.marketingRewardUsageCount(viewQuery, MarketingTypeEnum.DISCOUNT.getType());
		List<ActivityUsageCountDTO> limitUsageCount = activityCountDao.marketingLimitUsageCount(viewQuery, true);

		List<ActivityUsageCountDTO> marketingCouponCount = activityCountDao.marketingCouponCount(viewQuery, CouponProviderEnum.SHOP, true);

		//校验结果
		Map<Integer, List<ActivityUsageCountDTO>> countMap = checkReturn(activityMap, marketCountList, discountList, null, limitUsageCount, marketingCouponCount, null, true, viewQuery.getShopId());
		if (MapUtil.isEmpty(countMap)) {
			return null;
		}
		//注入活动次数
		countMap.forEach((k, v) -> {
			//遍历每个map
			activityList.stream().filter(e -> e.getType().equals(k)).findFirst().ifPresent(o -> {
				o.setUsage(v.get(0).getUsage() == null ? 0 : v.get(0).getUsage());
			});
		});

		return activityList;
	}

	private Map<Integer, List<ActivityUsageCountDTO>> getIntegerListMap(List<ActivityShopUsageDTO> activityList) {
		Map<Integer, List<ActivityUsageCountDTO>> activityMap = new HashMap<>(16);
		//8个类型
		List<MarketingTypeEnum> marketingTypeEnums = Arrays.asList(MarketingTypeEnum.values());
		// 初始化单个活动对象
		for (MarketingTypeEnum value : marketingTypeEnums) {
			ActivityShopUsageDTO activityShopUsageDTO = new ActivityShopUsageDTO();
			activityShopUsageDTO.setType(value.getType());
			activityList.add(activityShopUsageDTO);
			if (value.getType().equals(marketingTypeEnums.indexOf(MarketingTypeEnum.SHOP_COUPON))) {
				break;
			}
		}
		return activityMap;
	}


	/**
	 * 商家端：营销分析
	 */
	@Override
	public MasketingAnalysisDTO getMasketingAnalysis(MarketingDataViewQuery viewQuery) {
		//累计发布活动数
		MasketingAnalysisDTO masketingAnalysisDTO = new MasketingAnalysisDTO();
		List<ActivityShopUsageDTO> masketingTotalCount = getMasketingTotalCount(viewQuery);
		masketingAnalysisDTO.setTotalUsage(masketingTotalCount.stream().map(ActivityShopUsageDTO::getUsage).reduce((x, y) -> x + y).get());

		//营销支付金额         所有营销活动订单 营销支付金额=销售价-活动价
		List<ActivityAnalysisDTO> activityDTOList = activityCountDao.activitypayCount(viewQuery);
		//获取销售价大于活动价的
		List<ActivityAnalysisDTO> activityAnalysisDTO = activityDTOList.stream().filter(e -> e.getProductTotalAmount().compareTo(e.getActualAmount()) > 0).collect(Collectors.toList());
		BigDecimal reduce = activityAnalysisDTO.stream().map(x -> NumberUtil.sub(x.getProductTotalAmount(), x.getActualAmount())).reduce(BigDecimal.ZERO, BigDecimal::add);
		masketingAnalysisDTO.setPayAmount(reduce);

		//营销支付金额占比     营销支付金额 / 商品成交金额
		List<MarketingDataDealLineDTO> masketingDealLine = getMasketingDealLine(viewQuery);
		BigDecimal marketingAmount = masketingDealLine.stream().map(x -> NumberUtil.add(x.getCashAmount(), x.getDiscountAmount(), x.getCouponAmount())).reduce(BigDecimal.ZERO, BigDecimal::add);
		if (marketingAmount == null || marketingAmount.equals(BigDecimal.ZERO)) {
			masketingAnalysisDTO.setPayPercentage(BigDecimal.ZERO);
		} else {
			masketingAnalysisDTO.setPayPercentage(NumberUtil.div(NumberUtil.mul(masketingAnalysisDTO.getPayAmount(), BigDecimal.valueOf(100)), marketingAmount, 2, RoundingMode.HALF_UP));
		}
		//连带率
		ActivityAnalysisDTO activityDealRate = activityCountDao.activityDealRate(viewQuery);
		if (ObjectUtil.isEmpty(activityDealRate.getTotalCount())) {
			activityDealRate.setTotalCount(0);
		}
		if (ObjectUtil.isEmpty(activityDealRate.getCount()) || activityDealRate.getCount().equals(0)) {
			masketingAnalysisDTO.setRate(BigDecimal.ZERO);
		} else {
			masketingAnalysisDTO.setRate(NumberUtil.div(activityDealRate.getTotalCount(), activityDealRate.getCount(), 2, RoundingMode.HALF_UP));
		}

		return masketingAnalysisDTO;
	}

	/**
	 * 查询天数
	 */
	private long getBetween(MarketingDataViewQuery viewQuery) {
		// 设置查询日期
		if (viewQuery.getEndDate() == null && viewQuery.getStartDate() == null) {
			//未设置日期范围，默认七天
			viewQuery.setEndDate(DateUtil.endOfDay(DateUtil.offsetDay(new Date(), -1)));
			viewQuery.setStartDate(DateUtil.beginOfDay(DateUtil.offsetDay(viewQuery.getEndDate(), -6)));
		} else {
			viewQuery.setEndDate(DateUtil.endOfDay(viewQuery.getEndDate()));
			viewQuery.setStartDate(DateUtil.beginOfDay(viewQuery.getStartDate()));
		}
		return DateUtil.between(viewQuery.getStartDate(), viewQuery.getEndDate(), DateUnit.DAY) + 1L;
	}

	/**
	 * 校验结果
	 */
	private Map<Integer, List<ActivityUsageCountDTO>> checkReturn(Map<Integer, List<ActivityUsageCountDTO>> activityMap, List<ActivityUsageCountDTO> marketCountList, List<ActivityUsageCountDTO> discountList,
																  List<ActivityUsageCountDTO> countList, List<ActivityUsageCountDTO> limitUsageCount, List<ActivityUsageCountDTO> marketingCouponCount,
																  List<ActivityUsageCountDTO> platCouponCount, Boolean boot, Long shopId) {
		//false为折线、true为表格
		if (boot) {
			//满减
			if (CollectionUtils.isEmpty(marketCountList)) {
				activityMap.put(MarketingTypeEnum.CASH.getType(), initList());
			} else {
				activityMap.put(MarketingTypeEnum.CASH.getType(), marketCountList);
			}
			//满折
			if (CollectionUtils.isEmpty(discountList)) {
				activityMap.put(MarketingTypeEnum.DISCOUNT.getType(), initList());
			} else {
				activityMap.put(MarketingTypeEnum.DISCOUNT.getType(), discountList);
			}
		}

		if (!boot) {
			//满减满折
			if (CollectionUtils.isEmpty(countList)) {
				activityMap.put(MarketingTypeEnum.CASH_DISCOUNT.getType(), initList());
			} else {
				activityMap.put(MarketingTypeEnum.CASH_DISCOUNT.getType(), countList);
			}
		}

		//限时折扣
		if (CollectionUtils.isEmpty(limitUsageCount)) {
			activityMap.put(MarketingTypeEnum.LIMITED_TIME.getType(), initList());
		} else {
			activityMap.put(MarketingTypeEnum.LIMITED_TIME.getType(), limitUsageCount);
		}
		//商家优惠卷
		if (CollectionUtils.isEmpty(marketingCouponCount)) {
			activityMap.put(MarketingTypeEnum.SHOP_COUPON.getType(), initList());
		} else {
			activityMap.put(MarketingTypeEnum.SHOP_COUPON.getType(), marketingCouponCount);
		}
		//平台优惠卷
		if (shopId != null) {
			return activityMap;
		} else if (CollectionUtils.isEmpty(platCouponCount)) {
			activityMap.put(MarketingTypeEnum.PLAT_COUPON.getType(), initList());
		} else {
			activityMap.put(MarketingTypeEnum.PLAT_COUPON.getType(), platCouponCount);
		}

		return activityMap;
	}

	private List<ActivityUsageCountDTO> initList() {
		ActivityUsageCountDTO activityUsageCountDTO = new ActivityUsageCountDTO();

		List<ActivityUsageCountDTO> activityUsageCountList = new ArrayList<>();
		activityUsageCountList.add(new ActivityUsageCountDTO(0, BigDecimal.ZERO));
		return activityUsageCountList;
	}

	private void buildResult(MarketingDataViewLineDTO marketingDataViewLineDTO, MarketingDataDealLineDTO marketingDataDealLineDTO, Integer marketingType, ActivityUsageCountDTO o) {
		if (ObjectUtil.isNotEmpty(marketingDataViewLineDTO)) {
			switch (marketingType) {
				case 2:
					marketingDataViewLineDTO.setDiscountNum(o.getUsage());
					break;
				case 6:
					marketingDataViewLineDTO.setCouponNum(o.getUsage());
					break;
				case 7:
					marketingDataViewLineDTO.setPlatNum(o.getUsage());
					break;
				case 8:
					marketingDataViewLineDTO.setCashNum(o.getUsage());
					break;
			}
		}
		if (ObjectUtil.isNotEmpty(marketingDataDealLineDTO)) {
			switch (marketingType) {
				case 2:
					marketingDataDealLineDTO.setDiscountAmount(o.getAmount());
					break;
				case 6:
					marketingDataDealLineDTO.setCouponAmount(o.getAmount());
					break;
				case 7:
					marketingDataDealLineDTO.setPlatAmount(o.getAmount());
					break;
				case 8:
					marketingDataDealLineDTO.setCashAmount(o.getAmount());
					break;
			}
		}

	}

}
