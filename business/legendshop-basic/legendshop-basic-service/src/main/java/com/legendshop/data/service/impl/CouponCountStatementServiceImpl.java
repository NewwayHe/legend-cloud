/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.legendshop.activity.dto.MarketingDataTrendDTO;
import com.legendshop.data.dao.CouponCountStatementDao;
import com.legendshop.data.dto.ActivityCouponCountQuery;
import com.legendshop.data.dto.MarketingAmountStatisticsDTO;
import com.legendshop.data.dto.MarketingUserStatisticsDTO;
import com.legendshop.data.service.CouponCountStatementService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 营销信息查询 查找所有营销信息 统计成报表
 *
 * @author legendshop
 */
@Slf4j
@Service
@AllArgsConstructor
public class CouponCountStatementServiceImpl implements CouponCountStatementService {

	private final CouponCountStatementDao couponCountStatementDao;

	@Override
	public MarketingAmountStatisticsDTO marketingAmountStatistics(ActivityCouponCountQuery query) {
		if (ObjectUtil.isEmpty(query.getCouponId())) {
			return new MarketingAmountStatisticsDTO();
		}

		//平台营销支付金额,平台成交金额,平台营销利润金额,订单数量，购买数量
		MarketingAmountStatisticsDTO amount = couponCountStatementDao.getMarketingAmount(query);
		if (null == amount) {
			amount = new MarketingAmountStatisticsDTO();
		}

		//平台下单成交数和用户数
		MarketingAmountStatisticsDTO transactionAmount = couponCountStatementDao.getTransactionAmount(query);
		Optional<MarketingAmountStatisticsDTO> optional = Optional.ofNullable(transactionAmount);
		amount.setPayCount(optional.map(MarketingAmountStatisticsDTO::getPayCount).orElse(0L));
		amount.setDealCount(optional.map(MarketingAmountStatisticsDTO::getDealCount).orElse(0L));
		amount.setTransactionAmount(optional.map(MarketingAmountStatisticsDTO::getTransactionAmount).orElse(BigDecimal.ZERO));
		amount.setReturnOnInvestment(BigDecimal.ZERO);

		//平台roi回报率
		if (ObjectUtil.isNotEmpty(amount.getMarketingProfitAmount())
				&& ObjectUtil.isNotEmpty(amount.getMarketingPaymentAmount())
				&& BigDecimal.ZERO.compareTo(amount.getMarketingPaymentAmount()) != 0) {
			BigDecimal roi = NumberUtil.div(amount.getMarketingProfitAmount(), amount.getMarketingPaymentAmount(), 2, RoundingMode.DOWN);
			amount.setReturnOnInvestment(roi);
		}
		return amount;
	}

	@Override
	public MarketingUserStatisticsDTO marketingUserStatistics(ActivityCouponCountQuery query) {
		if (ObjectUtil.isEmpty(query.getCouponId())) {
			return new MarketingUserStatisticsDTO();
		}

		// 获取领取人数、领取次数
		MarketingUserStatisticsDTO statisticsDTO = couponCountStatementDao.getReceivedCount(query);
		if (null == statisticsDTO) {
			statisticsDTO = new MarketingUserStatisticsDTO();
		}

		// 下单订单数、下单用户数
		MarketingUserStatisticsDTO orderCount = couponCountStatementDao.getOrderCount(query);
		Optional<MarketingUserStatisticsDTO> orderCountOp = Optional.ofNullable(orderCount);
		statisticsDTO.setOrderCount(orderCountOp.map(MarketingUserStatisticsDTO::getOrderCount).orElse(0L));
		statisticsDTO.setUserOrderCount(orderCountOp.map(MarketingUserStatisticsDTO::getUserOrderCount).orElse(0L));

		// 获取 成交订单数 和 成交用户数
		MarketingAmountStatisticsDTO transactionAmount = couponCountStatementDao.getTransactionAmount(query);
		Optional<MarketingAmountStatisticsDTO> transactionAmountOp = Optional.ofNullable(transactionAmount);
		statisticsDTO.setPayCount(transactionAmountOp.map(MarketingAmountStatisticsDTO::getPayCount).orElse(0L));
		statisticsDTO.setUserPayCount(transactionAmountOp.map(MarketingAmountStatisticsDTO::getUserPayCount).orElse(0L));

		// 获取下单老用户数
		Long oldOrderCount = couponCountStatementDao.getOldOrderUserCount(query);
		statisticsDTO.setOrderOldUserCount(oldOrderCount);

		// 获取下单用户总数
		Long orderUserCount = statisticsDTO.getUserOrderCount();
		statisticsDTO.setOrderNewUserCount(orderUserCount - oldOrderCount);

		if (orderUserCount > 0) {
			statisticsDTO.setOrderNewUserRate(new BigDecimal(statisticsDTO.getOrderNewUserCount()).divide(new BigDecimal(orderUserCount), 4, RoundingMode.DOWN).multiply(new BigDecimal(100)));
			statisticsDTO.setOrderOldUserRate(new BigDecimal(statisticsDTO.getOrderOldUserCount()).divide(new BigDecimal(orderUserCount), 4, RoundingMode.DOWN).multiply(new BigDecimal(100)));
		}

		//平台转化率
		statisticsDTO.setInversionRate(BigDecimal.ZERO);
		if (ObjectUtil.isNotEmpty(statisticsDTO.getUserPayCount())
				&& ObjectUtil.isNotEmpty(statisticsDTO.getCouponsReceivedUser())
				&& 0 != statisticsDTO.getCouponsReceivedUser()) {
			BigDecimal inversionRate = NumberUtil.div(statisticsDTO.getUserPayCount(), statisticsDTO.getCouponsReceivedUser())
					.multiply(new BigDecimal(100)).setScale(2, RoundingMode.DOWN);
			statisticsDTO.setInversionRate(inversionRate);
		}
		return statisticsDTO;
	}

	@Override
	public List<MarketingDataTrendDTO> marketingDataTrendStatistics(ActivityCouponCountQuery query) {
		if (ObjectUtil.isEmpty(query.getCouponId())) {
			return Collections.emptyList();
		}

		//默认显示七天数据
		List<Date> dateAvg;
		if (query.getEndTime() == null && query.getBeginTime() == null) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			query.setBeginTime(s);
			query.setEndTime(DateUtil.endOfDay(e));
			dateAvg = getDateSplit(s, e);
		} else {
			dateAvg = getDateSplit(query.getBeginTime(), query.getEndTime());
		}

		List<MarketingDataTrendDTO> marketingDataTrend = new ArrayList<>();
		for (Date currentDate : dateAvg) {
			query.setBeginTime(DateUtil.beginOfDay(currentDate));
			query.setEndTime(DateUtil.endOfDay(currentDate));
			MarketingDataTrendDTO marketingDataTrendDTO = platformCouponDataMap(query);

			//添加数据
			marketingDataTrend.add(marketingDataTrendDTO);
		}

		return marketingDataTrend;
	}


	private MarketingDataTrendDTO platformCouponDataMap(ActivityCouponCountQuery query) {
		//组装数据
		MarketingDataTrendDTO dataTrend = new MarketingDataTrendDTO();
		dataTrend.setTime(DateUtil.formatDate(query.getBeginTime()));

		//新增成交金额,成交订单，成交数量
		MarketingAmountStatisticsDTO transactionAmount = couponCountStatementDao.getTransactionAmount(query);

		// 累计成交订单数,成交订单，成交数量
		List<MarketingDataTrendDTO> orderDealCountList = couponCountStatementDao.queryPlatformAmountList(query);

		// 新增下单用户数
		MarketingUserStatisticsDTO receivedCount = couponCountStatementDao.getOrderCount(query);

		//累计下单用户数
		List<MarketingDataTrendDTO> orderUserCountList = couponCountStatementDao.queryPlatformOrderUserCountList(query);

		//累计访问用户数和访问次数
		List<MarketingDataTrendDTO> visitCount = couponCountStatementDao.queryPlatformVisitCount(query);

		//新增访问用户数和访问次数
		List<MarketingDataTrendDTO> newVisitCount = couponCountStatementDao.queryPlatformNewVisitCount(query);

		// 获取领取人数
		MarketingUserStatisticsDTO statisticsDTO = couponCountStatementDao.getReceivedCount(query);
		MarketingUserStatisticsDTO totalReceivedCount = couponCountStatementDao.getTotalReceivedCount(query);


		if (ObjectUtil.isNotEmpty(transactionAmount)) {
			dataTrend.setNewTransactionAmount(transactionAmount.getTransactionAmount());
			dataTrend.setNewAddOrderCount(transactionAmount.getPayCount());
			dataTrend.setNewAddDealCount(transactionAmount.getDealCount());
		}
		if (CollUtil.isNotEmpty(orderDealCountList)) {
			MarketingDataTrendDTO amountLists = orderDealCountList.get(0);
			dataTrend.setTransactionAmountList(amountLists.getTransactionAmountList() == null ? BigDecimal.ZERO : amountLists.getTransactionAmountList());
			dataTrend.setOrderCountList(amountLists.getOrderCountList() == null ? 0L : amountLists.getOrderCountList());
			dataTrend.setDealCount(amountLists.getDealCount() == null ? 0L : amountLists.getDealCount());
		}

		if (ObjectUtil.isNotEmpty(receivedCount)) {
			dataTrend.setNewOrderUserCount(receivedCount.getUserOrderCount());
		}

		if (CollUtil.isNotEmpty(orderUserCountList)) {
			MarketingDataTrendDTO orderUserCountLists = orderUserCountList.get(0);
			dataTrend.setOrderUserCountList(orderUserCountLists.getOrderUserCountList() == null ? 0L : orderUserCountLists.getOrderUserCountList());
		}
		if (CollUtil.isNotEmpty(visitCount)) {
			MarketingDataTrendDTO visitCounts = visitCount.get(0);
			dataTrend.setVisitCountList(visitCounts.getVisitCountList() == null ? 0L : visitCounts.getVisitCountList());
			dataTrend.setVisitUserCountList(visitCounts.getVisitUserCountList() == null ? 0L : visitCounts.getVisitUserCountList());
		}
		if (CollUtil.isNotEmpty(newVisitCount)) {
			MarketingDataTrendDTO newVisitCounts = newVisitCount.get(0);
			dataTrend.setNewVisitCount(newVisitCounts.getNewVisitCount() == null ? 0L : newVisitCounts.getNewVisitCount());
			dataTrend.setNewVisitUserCount(newVisitCounts.getNewVisitUserCount() == null ? 0L : newVisitCounts.getNewVisitUserCount());
		}

		// 优惠券领券人数
		dataTrend.setNewReceivedUserCount(Optional.ofNullable(statisticsDTO).map(MarketingUserStatisticsDTO::getCouponsReceivedUser).orElse(0L));
		dataTrend.setTotalReceivedUserCount(Optional.ofNullable(totalReceivedCount).map(MarketingUserStatisticsDTO::getCouponsReceivedUser).orElse(0L));

		return dataTrend;
	}


	/**
	 * 根据天数获取--前num天至昨天23点59分59秒的date数组。
	 *
	 * @param num 天数
	 * @return date[0]-开始时间  date[1]-结束时间
	 */
	private Date[] getDate(Integer num) {

		String now = DateUtil.now();
		Date date = DateUtil.parse(now);

		Date beginOfDay = DateUtil.beginOfDay(date);
		Date startDate = DateUtil.offset(beginOfDay, DateField.DAY_OF_MONTH, -num);
		Date endDate = DateUtil.offset(beginOfDay, DateField.SECOND, -1);

		Date[] array = new Date[2];
		array[0] = startDate;
		array[1] = endDate;

		return array;
	}

	/**
	 * 将日期分割成以天为单位的list
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private List<Date> getDateSplit(Date startDate, Date endDate) {
		Integer between = Math.toIntExact(DateUtil.between(startDate, endDate, DateUnit.DAY));
		List<Integer> integerList = new ArrayList<>();
		for (int x = 1; x <= between; x++) {
			integerList.add(1);
		}
		List<Date> dateList = new ArrayList<>();
		dateList.add(startDate);
		AtomicReference<Date> temp = new AtomicReference<>(startDate);
		integerList.forEach(a -> {
			Date offset = DateUtil.offset(temp.get(), DateField.DAY_OF_YEAR, a);
			temp.set(offset);
			dateList.add(offset);
		});
		return dateList;
	}
}
