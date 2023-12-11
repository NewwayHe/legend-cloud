/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.*;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.dto.BaiDuMobileStatisticsSettingDTO;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.legendshop.data.config.BaiDuPropertiesDTO;
import com.legendshop.data.dao.*;
import com.legendshop.data.dto.*;
import com.legendshop.data.entity.*;
import com.legendshop.data.enums.ActivityTypeEnum;
import com.legendshop.data.query.ActivityDataQuery;
import com.legendshop.data.query.SimpleQuery;
import com.legendshop.data.service.DataService;
import com.legendshop.data.service.convert.*;
import com.legendshop.order.enums.OrderStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.legendshop.data.enums.ActivityTypeEnum.*;

/**
 * @author legendshop
 */
@Service
@Slf4j
public class DataServiceImpl implements DataService {

	@Autowired
	private SearchDataDao searchDataDao;

	@Autowired
	private DataDao dataDao;

	@Autowired
	private DataUserPurchasingDao purchasingDao;

	@Autowired
	private ProductViewDao productViewDao;

	@Autowired
	private ProductViewConverter productViewConverter;

	@Autowired
	private SearchHistoryConverter searchHistoryConverter;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private BaiDuPropertiesDTO baiDuPropertiesDTO;

	@Autowired
	private SysParamsApi sysParamsApi;

	@Autowired
	private ShopViewDao shopViewDao;

	@Autowired
	private ShopViewConverter shopViewConverter;

	@Autowired
	private AmqpSendMsgUtil amqpSendMsgUtil;

	@Autowired
	private BaiduViewDao baiduViewDao;

	@Autowired
	private DataActivityCollectDao dataActivityCollectDao;

	@Autowired
	private DataActivityCollectConverter dataActivityCollectConverter;

	@Autowired
	private DataActivityViewDao dataActivityViewDao;

	@Autowired
	private DataActivityViewConverter dataActivityViewConverter;

	@Autowired
	private DataActivityOrderDao dataActivityOrderDao;

	@Autowired
	private DataActivityOrderConverter dataActivityOrderConverter;

	@Autowired
	private DataUserAmountConverter dataUserAmountConverter;

	@Autowired
	private UserAmountDao userAmountDao;

	@Override
	public void saveSearchLog(SearchHistoryDTO dto) {

		String source = dto.getSource();
		SearchHistoryEntity from = searchHistoryConverter.from(dto);
		if (source == null || source.isEmpty()) {
			//source为空，不记录
			return;
		}
		searchDataDao.saveSearchLog(from);
	}

	@Override
	public void saveUserAmount(DataUserAmount userAmount) {

	}

	@Override
	public DataUserAmountDTO getLastedDataDate() {
		return dataDao.getLastedDataDate();
	}

	@Override
	public List<DataUserAmountDTO> getUserAmountNewData(Integer num) {
		Date[] date = getDate(1);
		Date s = date[0];
		Date e = date[1];
		ArrayList<DataUserAmountDTO> dtoList = new ArrayList<>();
		for (int x = num; x > 1; x--) {
			int temp = x - 2;
			Date startDate = DateUtil.offset(s, DateField.DAY_OF_YEAR, -temp);
			Date endDate = DateUtil.offset(e, DateField.DAY_OF_YEAR, -temp);
			DataUserAmountDTO dto = dataDao.getUserAmountNewData(startDate, endDate);
			if (dto.getUnknown() != null) {
				dto.setH5New(dto.getH5New() + dto.getUnknown());
			}
			dto.setCreateTime(startDate);
			Integer userCount = dataDao.getUserCount(endDate);
			dto.setPeopleAmount(userCount);
			dto.setPeopleNew(dto.getAppNew() + dto.getH5New() + dto.getMpNew() + dto.getMiniNew());
			dtoList.add(dto);
		}
		return dtoList;
	}

	@Override
	public boolean confirmUserHaveData() {
		Integer num = dataDao.getUserDetailNum();
		return num > 0;
	}

	@Override
	public boolean confirmPurchasingUserExist(Long userId) {
		Integer integer = dataDao.queryUserPurchasingExist(userId);
		return integer > 0;
	}

	@Override
	public void savePurchasingUserData(DataUserPurchasing data) {
		DataUserPurchasing source = purchasingDao.getByOrderId(data.getOrderId());
		if (StrUtil.isNotBlank(data.getSource()) && VisitSourceEnum.MP.name().equals(data.getSource())) {
			data.setSource(VisitSourceEnum.H5.name());
		}
		if (null == source) {
			//设置昵称和手机号
			NameAndMobileDTO e = dataDao.queryMobileById(data.getUserId());
			data.setNickName(e.getNickName());
			data.setMobile(e.getMobile());
			purchasingDao.save(data);
		} else {
			BeanUtil.copyProperties(source, data, CopyOptions.create().setIgnoreNullValue(true));
			purchasingDao.update(data);
		}
	}

	@Override
	public DataUserPurchasing getPurchasingDataById(Long userId) {
		return dataDao.queryPurchasingDataById(userId);
	}

	@Override
	public void saveViewData(ProdViewMqDTO dto) {
		ProductViewDTO viewDTO = dto.getProductViewDTO();
		//根据商品id,时间和来源查询是否存在今天记录
		String source = dto.getSource();
		if (VisitSourceEnum.MP.name().equals(source)) {
			source = VisitSourceEnum.H5.name();
		}
		Long productId = viewDTO.getProductId();
		Date createTime = viewDTO.getCreateTime();
		ProductViewDTO productViewDTO = dataDao.queryViewDataById(productId, createTime, source);
		Long userId = dto.getUserId();
		if (source == null || source.isEmpty() || userId == null) {
			//source为空，不记录
			return;
		}
		if (productViewDTO != null) {
			long between = DateUtil.between(DateUtil.date(), DateUtil.beginOfDay(DateUtil.offsetDay(createTime, 1)), DateUnit.SECOND, true);
			log.info("离当天结束还有 {} 秒", between);
			//数据库有该商品记录，根据来源更新浏览信息
			switch (source) {
				case "MP":
					//根据用户id+source+商品id查询是否浏览过
					//redis没有用户该端记录，人数+1
					if (!redisTemplate.hasKey(userId + "MP" + productId)) {
						productViewDTO.setViewPeople(productViewDTO.getViewPeople() + 1);
						//保存浏览用户到redis
						redisTemplate.opsForValue().set(userId + "MP" + productId, 1, between, TimeUnit.SECONDS);
					}
					break;
				case "MINI":
					if (!redisTemplate.hasKey(userId + "MINI" + productId)) {
						productViewDTO.setViewPeople(productViewDTO.getViewPeople() + 1);
						redisTemplate.opsForValue().set(userId + "MINI" + productId, userId + "MINI" + productId, between, TimeUnit.SECONDS);
					}
					break;
				case "APP":
					if (!redisTemplate.hasKey(userId + "App" + productId)) {
						productViewDTO.setViewPeople(productViewDTO.getViewPeople() + 1);
						redisTemplate.opsForValue().set(userId + "App" + productId, userId + "App" + productId, between, TimeUnit.SECONDS);
					}
					break;
				case "H5":
					if (!redisTemplate.hasKey(userId + "H5" + productId)) {
						productViewDTO.setViewPeople(productViewDTO.getViewPeople() + 1);
						redisTemplate.opsForValue().set(userId + "H5" + productId, userId + "H5" + productId, between, TimeUnit.SECONDS);
					}
					break;
				default:

			}
			productViewDTO.setViewFrequency(productViewDTO.getViewFrequency() + 1);
			ProductView from = productViewConverter.from(productViewDTO);
			productViewDao.updateProperties(from);
		} else {
			//数据库没有该商品记录，保存浏览信息并继续调用该方法更新来源记录
			viewDTO.setCreateTime(DateUtil.beginOfDay(new Date()))
					.setViewFrequency(0)
					.setViewPeople(0)
					.setSource(source)
					.setCartNum(0);
			ProductView entity = productViewConverter.from(viewDTO);
			productViewDao.save(entity);
			saveViewData(dto);
		}

	}

	@Override
	public Integer[] getBaiDuTongData(String startDate, String endDate) {
		return baiDuTongJi(startDate, endDate);
	}

	@Override
	public Integer[] getBaiDuTongDataString(Date startDate, Date endDate) {

		return baiDuTongJi(DateUtil.formatDateTime(startDate), DateUtil.formatDateTime(endDate));
	}

	@Override
	public Integer[] getBaiDuTongDataString(Date startDate, Date endDate, String source) {
		BaiduView baiduView = baiduViewDao.getAllByArchiveTime(startDate, endDate);
		Integer[] result = {0, 0};
		if (null == baiduView) {
			return result;
		}
		VisitSourceEnum sourceEnum = VisitSourceEnum.getByName(source);
		switch (sourceEnum) {
			case MP:
			case PC:
			case H5:
				result[0] = baiduView.getH5Pv();
				result[1] = baiduView.getH5Uv();
				return result;
			case MINI:
				result[0] = baiduView.getMiniPv();
				result[1] = baiduView.getMiniUv();
				return result;
			case APP:
				result[0] = 0;
				result[1] = 0;
				return result;
			default:
				// 为空，则查询全部
				result[0] = baiduView.getTotalPv();
				result[1] = baiduView.getTotalUv();
				return result;
		}
	}

	@Override
	public Integer[] getBaiDuMobileTongData(String startDate, String endDate, String source) {
		return baiDuMobileTongJi(startDate, endDate, source);
	}

	@Override
	public Integer[] getBaiDuMobileTongDataString(Date startDate, Date endDate, String source) {
		return baiDuMobileTongJi(DateUtil.formatDateTime(startDate), DateUtil.formatDateTime(endDate), source);
	}

	@Override
	public BusinessDataDTO getBusinessData(SimpleQuery simpleQuery) {

		if (simpleQuery.getEndDate() == null || simpleQuery.getStartDate() == null) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			simpleQuery.setStartDate(s);
			simpleQuery.setEndDate(DateUtil.endOfDay(e));
		} else {
			simpleQuery.setStartDate(DateUtil.beginOfDay(simpleQuery.getStartDate()));
			simpleQuery.setEndDate(DateUtil.endOfDay(simpleQuery.getEndDate()));
		}
		BusinessDataDTO dto = dataDao.queryBusinessData(simpleQuery);
		Integer pv;
		Integer uv;
		if (simpleQuery.getShopId() != null) {
			ShopViewDTO shopViewPic = dataDao.getShopViewPic(simpleQuery.getShopId(), simpleQuery.getStartDate(), simpleQuery.getEndDate(), simpleQuery.getSource());
			pv = shopViewPic.getViewFrequency();
			uv = shopViewPic.getViewPeople();
		} else {
			Integer[] baiDuTongData = getBaiDuTongDataString(simpleQuery.getStartDate(), simpleQuery.getEndDate(), simpleQuery.getSource());
			pv = baiDuTongData[0];
			uv = baiDuTongData[1];
		}
		ProductViewDTO prodViewNum = getProdViewNum(simpleQuery);
		Integer favoriteNum = getFavoriteNum(simpleQuery);
		dto.setPv(pv);
		dto.setUv(uv);
		dto.setProductPv(prodViewNum.getViewFrequency());
		dto.setProductUv(prodViewNum.getViewPeople());
		dto.setCartSkuNum(Optional.ofNullable(prodViewNum.getCartNum()).orElse(0));
		dto.setFavoriteNum(favoriteNum);
		//计算各种转换率
		if (dto.getDealUserNum() != null && dto.getProductUv() != 0) {
			dto.setInversionRate(BigDecimal.valueOf(dto.getDealUserNum()).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(dto.getProductUv()), 2, RoundingMode.HALF_UP));
		} else {
			dto.setInversionRate(BigDecimal.ZERO);
		}
		if (ObjectUtil.isNotEmpty(dto.getCartSkuNum()) && dto.getCartSkuNum() != 0) {
			dto.setAddPlaceRate(BigDecimal.valueOf(dto.getPlaceUserNum()).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(dto.getCartSkuNum()), 2, RoundingMode.HALF_UP));
		} else {
			dto.setAddPlaceRate(BigDecimal.ZERO);
		}
		if (ObjectUtil.isNotEmpty(dto.getPayUserNum()) && dto.getPayUserNum() != 0) {
			dto.setDealPayRate(BigDecimal.valueOf(dto.getDealUserNum()).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(dto.getPayUserNum()), 2, RoundingMode.HALF_UP));
		} else {
			dto.setDealPayRate(BigDecimal.ZERO);
		}
		if (ObjectUtil.isNotEmpty(dto.getPlaceUserNum()) && dto.getPlaceUserNum() != 0) {
			dto.setPlacePayRate(BigDecimal.valueOf(dto.getPayUserNum()).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(dto.getPlaceUserNum()), 2, RoundingMode.HALF_UP));
		} else {
			dto.setPlacePayRate(BigDecimal.ZERO);
		}
		if (ObjectUtil.isNotEmpty(dto.getProductUv()) && dto.getProductUv() != 0) {
			dto.setViewAddRate(BigDecimal.valueOf(dto.getCartSkuNum()).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(dto.getProductUv()), 2, RoundingMode.HALF_UP));
		} else {
			dto.setViewAddRate(BigDecimal.ZERO);
		}
		return dto;
	}

	@Override
	public ProductViewDTO getProdViewNum(SimpleQuery simpleQuery) {
		return dataDao.queryProdViewNum(simpleQuery.getSource(), simpleQuery.getStartDate(), simpleQuery.getEndDate(), simpleQuery.getShopId());
	}

	@Override
	public Integer getCartNum(Date startDate, Date endDate, Long shopId) {
		return dataDao.queryCartNum(startDate, endDate, shopId);
	}

	@Override
	public Integer getFavoriteNum(SimpleQuery simpleQuery) {
		return dataDao.queryFavoriteNum(simpleQuery);
	}

	@Override
	@Cacheable(value = "getViewPic")
	public List<ViewPicDTO> getViewPic(SimpleQuery simpleQuery) {
		List<Date> dateAvg;
		if (simpleQuery.getEndDate() == null && simpleQuery.getStartDate() == null) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			simpleQuery.setStartDate(s);
			simpleQuery.setEndDate(DateUtil.endOfDay(e));
			dateAvg = getDateSplit(s, e);
		} else {
			dateAvg = getDateSplit(simpleQuery.getStartDate(), simpleQuery.getEndDate());
		}
		List<ViewPicDTO> list = new ArrayList<>();
		for (int x = 0; x < dateAvg.size() - 1; x++) {
			Integer[] integers = getBaiDuTongDataString(dateAvg.get(x), dateAvg.get(x + 1), simpleQuery.getSource());
			ViewPicDTO dto = new ViewPicDTO();
			dto.setPv(integers[0]);
			dto.setUv(integers[1]);
			dto.setCreateTime(dateAvg.get(x));
			list.add(dto);
		}

		return list;
	}

	@Override
	@Cacheable(value = "getShopViewPic")
	public List<ViewPicDTO> getShopViewPic(SimpleQuery simpleQuery) {
		List<Date> dateAvg;
		if (simpleQuery.getEndDate() == null && simpleQuery.getStartDate() == null) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			simpleQuery.setStartDate(s);
			simpleQuery.setEndDate(DateUtil.endOfDay(e));
			dateAvg = getDateSplit(s, e);
		} else {
			dateAvg = getDateSplit(simpleQuery.getStartDate(), simpleQuery.getEndDate());
		}
		List<ViewPicDTO> list = new ArrayList<>();
		for (int x = 0; x < dateAvg.size() - 1; x++) {
			ShopViewDTO shopViewPic = dataDao.getShopViewPic(simpleQuery.getShopId(), dateAvg.get(x), dateAvg.get(x + 1), null);
			ViewPicDTO dto = new ViewPicDTO();
			dto.setPv(shopViewPic.getViewFrequency());
			dto.setUv(shopViewPic.getViewPeople());
			dto.setCreateTime(dateAvg.get(x));
			list.add(dto);
		}
		return list;
	}

	@Override
	public List<DealOrderPicDTO> getDealOrderPic(SimpleQuery simpleQuery) {
		List<Date> dateAvg;
		if (simpleQuery.getEndDate() == null && simpleQuery.getStartDate() == null) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			simpleQuery.setStartDate(s);
			simpleQuery.setEndDate(DateUtil.endOfDay(e));
			dateAvg = getDateSplit(s, e);
		} else {
			dateAvg = getDateSplit(simpleQuery.getStartDate(), simpleQuery.getEndDate());
		}
		List<DealOrderPicDTO> list = new ArrayList<>();
		for (int x = 0; x < dateAvg.size() - 1; x++) {
			DealOrderPicDTO dto = dataDao.queryDealOrderPic(dateAvg.get(x), dateAvg.get(x + 1), simpleQuery.getSource(), simpleQuery.getShopId());
			if (dto != null) {
				dto.setCreateTime(dateAvg.get(x));
				dto.setDealOrderAmount(Optional.ofNullable(dto.getDealOrderAmount()).orElse(BigDecimal.ZERO));
			}
			list.add(dto);
		}

		return list;
	}

	@Override
	public List<ReturnOrderPicDTO> getReturnOrderPic(SimpleQuery simpleQuery) {
		List<Date> dateAvg;
		if (simpleQuery.getEndDate() == null && simpleQuery.getStartDate() == null) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			simpleQuery.setStartDate(s);
			simpleQuery.setEndDate(DateUtil.endOfDay(e));
			dateAvg = getDateSplit(s, e);
		} else {
			dateAvg = getDateSplit(simpleQuery.getStartDate(), simpleQuery.getEndDate());
		}
		List<ReturnOrderPicDTO> list = new ArrayList<>();
		for (int x = 0; x < dateAvg.size() - 1; x++) {
			Integer refundNum = dataDao.queryReturnOrderPic(dateAvg.get(x), dateAvg.get(x + 1), 1, simpleQuery.getShopId());
			Integer returnNum = dataDao.queryReturnOrderPic(dateAvg.get(x), dateAvg.get(x + 1), 2, simpleQuery.getShopId());
			Integer num = dataDao.queryPayGoodNum(dateAvg.get(x), dateAvg.get(x + 1), simpleQuery.getShopId());
			ReturnOrderPicDTO dto = new ReturnOrderPicDTO();
			dto.setRefundRate(BigDecimal.ZERO);
			dto.setReturnRate(BigDecimal.ZERO);
			if (refundNum != null && num != null) {
				dto.setRefundRate(BigDecimal.valueOf(refundNum).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(num), 2, RoundingMode.HALF_UP));
			}
			if (returnNum != null && num != null) {
				dto.setReturnRate(BigDecimal.valueOf(returnNum).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(num), 2, RoundingMode.HALF_UP));
			}
			dto.setCreateTime(dateAvg.get(x));
			list.add(dto);
		}
		return list;
	}

	@Override
	public UserDataViewsDTO getUserViewData(String source) {
		if (StrUtil.isNotBlank(source) && "App".equals(source)) {
			source = "APP";
		}
		VisitSourceEnum sourceEnum = VisitSourceEnum.getByName(source);
		switch (sourceEnum) {
			case H5:
				return getH5UserViewData();
			case MINI:
				return getMiniOrAppUserViewData();
			case APP:
				// TODO 百度移动统计不支持uni打包的APP
				return new UserDataViewsDTO();
			default:
				// 为空，则查询全部
				UserDataViewsDTO h5UserViewData = getH5UserViewData();
				UserDataViewsDTO miniOrAppUserViewData = getMiniOrAppUserViewData();

				h5UserViewData.setUserViews(Optional.ofNullable(h5UserViewData.getUserViews()).orElse(0) + Optional.ofNullable(miniOrAppUserViewData.getUserViews()).orElse(0));
				h5UserViewData.setUserViewsByDay(Optional.ofNullable(h5UserViewData.getUserViewsByDay()).orElse(0) + Optional.ofNullable(miniOrAppUserViewData.getUserViewsByDay()).orElse(0));
				h5UserViewData.setUserViewsByWeek(Optional.ofNullable(h5UserViewData.getUserViewsByWeek()).orElse(0) + Optional.ofNullable(miniOrAppUserViewData.getUserViewsByWeek()).orElse(0));
				h5UserViewData.setUserViewsByMonth(Optional.ofNullable(h5UserViewData.getUserViewsByMonth()).orElse(0) + Optional.ofNullable(miniOrAppUserViewData.getUserViewsByMonth()).orElse(0));
				return h5UserViewData;
		}
	}

	private UserDataViewsDTO getMiniOrAppUserViewData() {
		return getMiniOrAppUserViewData(null, null);
	}

	@Override
	public void saveShopView(ShopViewDTO shopViewDTO) {
		//根据店铺id,时间和来源查询是否存在今天记录
		String source = shopViewDTO.getSource();
		if (VisitSourceEnum.MP.name().equals(source)) {
			source = VisitSourceEnum.H5.name();
		}
		Long shopId = shopViewDTO.getShopId();
		Date createTime = shopViewDTO.getCreateTime();
		ShopViewDTO shopViewDataById = dataDao.queryShopViewDataById(shopId, createTime, source);
		Long userId = shopViewDTO.getUserId();
		if (source == null || source.isEmpty()) {
			//source为空，不记录
			return;
		}
		if (shopViewDataById != null) {
			long between = DateUtil.between(DateUtil.date(), DateUtil.beginOfDay(DateUtil.offsetDay(createTime, 1)), DateUnit.SECOND);
			log.info("离当天结束还有 {} 秒", between);
			//数据库有该店铺记录，根据来源更新浏览信息
			switch (source) {
				case "MP":
					//根据用户id+source+商品id查询是否浏览过
					//redis没有用户该端记录，人数+1
					if (!redisTemplate.hasKey(userId + "MP" + shopId)) {
						shopViewDataById.setViewPeople(shopViewDataById.getViewPeople() + 1);
						//保存浏览用户到redis
						redisTemplate.opsForValue().set(userId + "MP" + shopId, 1, between, TimeUnit.SECONDS);
					}
					break;
				case "MINI":
					if (!redisTemplate.hasKey(userId + "MINI" + shopId)) {
						shopViewDataById.setViewPeople(shopViewDataById.getViewPeople() + 1);
						redisTemplate.opsForValue().set(userId + "MINI" + shopId, userId + "MINI" + shopId, between, TimeUnit.SECONDS);
					}
					break;
				case "APP":
					if (!redisTemplate.hasKey(userId + "App" + shopId)) {
						shopViewDataById.setViewPeople(shopViewDataById.getViewPeople() + 1);
						redisTemplate.opsForValue().set(userId + "App" + shopId, userId + "App" + shopId, between, TimeUnit.SECONDS);
					}
					break;
				case "H5":
					if (!redisTemplate.hasKey(userId + "H5" + shopId)) {
						shopViewDataById.setViewPeople(shopViewDataById.getViewPeople() + 1);
						redisTemplate.opsForValue().set(userId + "H5" + shopId, userId + "H5" + shopId, between, TimeUnit.SECONDS);
					}
					break;
				default:

			}
			shopViewDataById.setViewFrequency(shopViewDataById.getViewFrequency() + 1);
			ShopView from = shopViewConverter.from(shopViewDataById);
			shopViewDao.updateProperties(from);
		} else {
			//数据库没有该商品记录，保存浏览信息并继续调用该方法更新来源记录
			shopViewDTO.setViewFrequency(0)
					.setViewPeople(0)
					.setSource(source);
			ShopView entity = shopViewConverter.from(shopViewDTO);
			shopViewDao.save(entity);
			saveShopView(shopViewDTO);
		}
	}

	@Override
	public void saveViewDataToCart(ProdViewMqDTO dto) {
		ProductViewDTO viewDTO = dto.getProductViewDTO();
		//根据商品id,时间和来源查询是否存在今天记录
		String source = dto.getSource();
		if (StrUtil.isEmpty(source) || VisitSourceEnum.MP.name().equals(source)) {
			source = VisitSourceEnum.H5.name();
		}
		Long productId = viewDTO.getProductId();
		Date createTime = viewDTO.getCreateTime();
		ProductViewDTO productViewDTO = dataDao.queryViewDataById(productId, createTime, source);

		if (productViewDTO == null) {
			//数据库没有该商品记录，保存浏览信息并继续调用该方法更新来源记录
			viewDTO.setCreateTime(DateUtil.beginOfDay(new Date()))
					.setViewFrequency(0)
					.setViewPeople(0)
					.setSource(source);
			ProductView entity = productViewConverter.from(viewDTO);
			productViewDao.save(entity);
		} else {
			productViewDTO.setCartNum(Optional.ofNullable(productViewDTO.getCartNum()).orElse(0) + Optional.ofNullable(viewDTO.getCartNum()).orElse(0));
			productViewDao.update(productViewConverter.from(productViewDTO));
		}
	}

	@Override
	public DataActivityCollectDTO getLastedCollectData() {
		return dataDao.getLastedActivityData();
	}

	@Override
	public Boolean activityCollect(Boolean flag) {

		if (flag) {
			List<DataActivityCollectDTO> couponCollect = couponCollect(null, null);
			dataActivityCollectDao.save(dataActivityCollectConverter.from(couponCollect));
		} else {
			Date[] date = getDate(1);
			List<DataActivityCollectDTO> couponCollect = couponCollect(date[0], date[1]);
			dataActivityCollectDao.save(dataActivityCollectConverter.from(couponCollect));
		}
		return true;
	}


	@Override
	public List<DataActivityCollectDTO> couponCollect(Date startDate, Date endDate) {
		List<ActivityCollectDTO> activityCollectDTOList;

		if (startDate == null && endDate == null) {
			//获取所有优惠券数据
			activityCollectDTOList = dataDao.couponCollect(null, null, null);

		} else {
			//查询原本未完成的汇总
			List<DataActivityCollectDTO> unFinishedCollect = dataActivityCollectDao.getUnFinishedCollect("COUPON");
			activityCollectDTOList = dataDao.couponCollect(startDate, endDate, null);
			if (CollUtil.isNotEmpty(unFinishedCollect)) {
				List<Long> unFinishedId = unFinishedCollect.stream().map(DataActivityCollectDTO::getActivityId).collect(Collectors.toList());
				List<ActivityCollectDTO> activityCollectDTOList1 = dataDao.couponCollect(null, null, unFinishedId);
				if (CollUtil.isNotEmpty(activityCollectDTOList)) {
					activityCollectDTOList.addAll(activityCollectDTOList1);
				} else {
					activityCollectDTOList = activityCollectDTOList1;
				}
			}
		}

		if (CollUtil.isEmpty(activityCollectDTOList)) {
			return null;
		}

		activityCollectDTOList = activityCollectDTOList.stream().distinct().collect(Collectors.toList());

		Map<Integer, List<ActivityCollectDTO>> map = activityCollectDTOList.stream().collect(Collectors.groupingBy(ActivityCollectDTO::getShopProviderFlag));
		//平台优惠券
		List<ActivityCollectDTO> plartformList = map.get(0);
		Map<Long, List<ActivityCollectDTO>> plartformMap = plartformList.stream().collect(Collectors.groupingBy(ActivityCollectDTO::getActivityId));
		List<Long> plartformIdList = plartformList.stream().map(ActivityCollectDTO::getActivityId).distinct().collect(Collectors.toList());

		//店铺优惠券
		List<ActivityCollectDTO> shopList = map.get(1);
		Map<Long, List<ActivityCollectDTO>> shopMap = shopList.stream().collect(Collectors.groupingBy(ActivityCollectDTO::getActivityId));
		List<Long> shopIdList = shopList.stream().map(ActivityCollectDTO::getActivityId).distinct().collect(Collectors.toList());

		List<DataActivityCollectDTO> resultList = new ArrayList<>();
		List<DataActivityOrderDTO> activityOrderDtoList = new ArrayList<>();

		if (CollUtil.isNotEmpty(plartformIdList)) {
			plartformIdList.forEach(a -> {

				List<ActivityCollectDTO> list = plartformMap.get(a);
				list.forEach(dto -> {
					DataActivityOrderDTO dataActivityOrderDTO = new DataActivityOrderDTO();
					dataActivityOrderDTO.setActivityId(dto.getActivityId());
					dataActivityOrderDTO.setActivityType(PLARTFORM_COUPON.name());
					dataActivityOrderDTO.setOrderId(dto.getOrderId());
					dataActivityOrderDTO.setOrderItemId(dto.getOrderItemId());
					dataActivityOrderDTO.setAmount(dto.getActualTotalPrice());
					dataActivityOrderDTO.setBasketCount(dto.getBasketCount());
					activityOrderDtoList.add(dataActivityOrderDTO);
				});
				DataActivityCollectDTO resultDTO = new DataActivityCollectDTO();

				resultDTO.setType(ActivityTypeEnum.PLARTFORM_COUPON.name());
				resultDTO.setActivityId(list.get(0).getActivityId());
				resultDTO.setCreateTime(list.get(0).getCreateTime());
				resultDTO.setShopId(list.get(0).getShopId());
				resultDTO.setOpStatus(list.get(0).getOpStatus());
				resultDTO.setActivityStatus(list.get(0).getActivityStatus());

				Optional<BigDecimal> reduce = list.stream().map(ActivityCollectDTO::getActualTotalPrice).filter(Objects::nonNull).reduce(BigDecimal::add);
				BigDecimal sum = BigDecimal.ZERO;
				if (reduce.isPresent()) {
					sum = reduce.get();
				}
				if ((DateUtil.compare(DateUtil.date(), list.get(0).getEndTime()) >= 0) && Objects.equals(sum, BigDecimal.ZERO)) {

					resultDTO.setViewFrequency(0);
					resultDTO.setViewPeople(0);
					resultDTO.setDealNewUser(0);
					resultDTO.setDealOldUser(0);
					resultDTO.setDealAmount(BigDecimal.ZERO);
					resultDTO.setDealNumber(0);
					resultDTO.setDealUserNum(0);
					resultDTO.setStatus(-1);

				} else if (DateUtil.compare(DateUtil.date(), list.get(0).getEndTime()) < 0) {

					BigDecimal dealAmount = BigDecimal.ZERO;
					int dealNumber = 0;
					List<Long> userList = new ArrayList<>();
					List<Long> newUserList = new ArrayList<>();
					for (ActivityCollectDTO b : list) {
						if (Objects.equals(b.getOrderStatus(), OrderStatusEnum.SUCCESS.getValue()) && !Objects.equals(b.getRefundStatus(), 2)) {
							dealAmount = dealAmount.add(b.getActualTotalPrice());
							dealNumber = dealNumber + b.getProductQuantity();
							userList.add(b.getUserId());
							if (dataDao.queryIfNewCustomer(b.getUserId(), b.getOrderTime())) {
								newUserList.add(b.getUserId());
							}
						}
					}
					resultDTO.setDealAmount(dealAmount);
					resultDTO.setDealNumber(dealNumber);
					resultDTO.setDealUserNum((int) userList.stream().distinct().count());
					resultDTO.setDealNewUser((int) newUserList.stream().distinct().count());
					resultDTO.setDealOldUser(resultDTO.getDealUserNum() - resultDTO.getDealNewUser());
					resultDTO.setStatus(0);

				} else if ((DateUtil.compare(DateUtil.date(), list.get(0).getEndTime()) >= 0) && !Objects.equals(sum, BigDecimal.ZERO)) {

					BigDecimal dealAmount = BigDecimal.ZERO;
					int dealNumber = 0;
					List<Long> userList = new ArrayList<>();
					List<Long> newUserList = new ArrayList<>();
					boolean flag = true;
					for (ActivityCollectDTO b : list) {
						if (Objects.equals(b.getOrderStatus(), OrderStatusEnum.SUCCESS.getValue()) && !Objects.equals(b.getRefundStatus(), 2)) {
							dealAmount = dealAmount.add(b.getActualTotalPrice());
							dealNumber = dealNumber + b.getProductQuantity();
							userList.add(b.getUserId());
							if (dataDao.queryIfNewCustomer(b.getUserId(), b.getOrderTime())) {
								newUserList.add(b.getUserId());
							}
							if (DateUtil.compare(DateUtil.date(), b.getFinalReturnDeadline()) <= 0) {
								flag = false;
							}
						}
						if (!Objects.equals(b.getOrderStatus(), OrderStatusEnum.SUCCESS.getValue())) {
							flag = false;
						}
					}
					if (flag) {
						resultDTO.setStatus(1);
					} else {
						resultDTO.setStatus(0);
					}
					resultDTO.setDealAmount(dealAmount);
					resultDTO.setDealNumber(dealNumber);
					resultDTO.setDealUserNum((int) userList.stream().distinct().count());
					resultDTO.setDealNewUser((int) newUserList.stream().distinct().count());
					resultDTO.setDealOldUser(resultDTO.getDealUserNum() - resultDTO.getDealNewUser());
				}
				resultList.add(resultDTO);
			});
		}

		if (CollUtil.isNotEmpty(shopIdList)) {
			shopIdList.forEach(a -> {

				List<ActivityCollectDTO> list = shopMap.get(a);
				list.forEach(dto -> {
					DataActivityOrderDTO dataActivityOrderDTO = new DataActivityOrderDTO();
					dataActivityOrderDTO.setActivityId(dto.getActivityId());
					dataActivityOrderDTO.setActivityType(SHOP_COUPON.name());
					dataActivityOrderDTO.setOrderId(dto.getOrderId());
					dataActivityOrderDTO.setOrderItemId(dto.getOrderItemId());
					dataActivityOrderDTO.setAmount(dto.getActualTotalPrice());
					dataActivityOrderDTO.setBasketCount(dto.getBasketCount());
					activityOrderDtoList.add(dataActivityOrderDTO);
				});
				DataActivityCollectDTO resultDTO = new DataActivityCollectDTO();

				resultDTO.setType(ActivityTypeEnum.SHOP_COUPON.name());
				resultDTO.setActivityId(list.get(0).getActivityId());
				resultDTO.setCreateTime(list.get(0).getCreateTime());
				resultDTO.setShopId(list.get(0).getShopId());
				resultDTO.setOpStatus(list.get(0).getOpStatus());
				resultDTO.setActivityStatus(list.get(0).getActivityStatus());

				Optional<BigDecimal> reduce = list.stream().map(ActivityCollectDTO::getActualTotalPrice).filter(Objects::nonNull).reduce(BigDecimal::add);
				BigDecimal sum = BigDecimal.ZERO;
				if (reduce.isPresent()) {
					sum = reduce.get();
				}
				if ((DateUtil.compare(DateUtil.date(), list.get(0).getEndTime()) >= 0) && Objects.equals(sum, BigDecimal.ZERO)) {

					resultDTO.setViewFrequency(0);
					resultDTO.setViewPeople(0);
					resultDTO.setDealNewUser(0);
					resultDTO.setDealOldUser(0);
					resultDTO.setDealAmount(BigDecimal.ZERO);
					resultDTO.setDealNumber(0);
					resultDTO.setDealUserNum(0);
					resultDTO.setStatus(-1);

				} else if (DateUtil.compare(DateUtil.date(), list.get(0).getEndTime()) < 0) {

					BigDecimal dealAmount = BigDecimal.ZERO;
					int dealNumber = 0;
					List<Long> userList = new ArrayList<>();
					List<Long> newUserList = new ArrayList<>();
					for (ActivityCollectDTO b : list) {
						if (Objects.equals(b.getOrderStatus(), OrderStatusEnum.SUCCESS.getValue()) && !Objects.equals(b.getRefundStatus(), 2)) {
							dealAmount = dealAmount.add(b.getActualTotalPrice());
							dealNumber = dealNumber + b.getProductQuantity();
							userList.add(b.getUserId());
							if (dataDao.queryIfNewCustomer(b.getUserId(), b.getOrderTime())) {
								newUserList.add(b.getUserId());
							}
						}
					}
					resultDTO.setDealAmount(dealAmount);
					resultDTO.setDealNumber(dealNumber);
					resultDTO.setDealUserNum((int) userList.stream().distinct().count());
					resultDTO.setDealNewUser((int) newUserList.stream().distinct().count());
					resultDTO.setDealOldUser(resultDTO.getDealUserNum() - resultDTO.getDealNewUser());
					resultDTO.setStatus(0);

				} else if ((DateUtil.compare(DateUtil.date(), list.get(0).getEndTime()) >= 0) && !Objects.equals(sum, BigDecimal.ZERO)) {

					BigDecimal dealAmount = BigDecimal.ZERO;
					int dealNumber = 0;
					List<Long> userList = new ArrayList<>();
					List<Long> newUserList = new ArrayList<>();
					boolean flag = true;
					for (ActivityCollectDTO b : list) {
						if (Objects.equals(b.getOrderStatus(), OrderStatusEnum.SUCCESS.getValue()) && !Objects.equals(b.getRefundStatus(), 2)) {
							dealAmount = dealAmount.add(b.getActualTotalPrice());
							dealNumber = dealNumber + b.getProductQuantity();
							userList.add(b.getUserId());
							if (dataDao.queryIfNewCustomer(b.getUserId(), b.getOrderTime())) {
								newUserList.add(b.getUserId());
							}
							if (DateUtil.compare(DateUtil.date(), b.getFinalReturnDeadline()) <= 0) {
								flag = false;
							}
						}
						if (!Objects.equals(b.getOrderStatus(), OrderStatusEnum.SUCCESS.getValue())) {
							flag = false;
						}
					}
					if (flag) {
						resultDTO.setStatus(1);
					} else {
						resultDTO.setStatus(0);
					}
					resultDTO.setDealAmount(dealAmount);
					resultDTO.setDealNumber(dealNumber);
					resultDTO.setDealUserNum((int) userList.stream().distinct().count());
					resultDTO.setDealNewUser((int) newUserList.stream().distinct().count());
					resultDTO.setDealOldUser(resultDTO.getDealUserNum() - resultDTO.getDealNewUser());
				}
				resultList.add(resultDTO);
			});
		}

		dataActivityOrderDao.save(dataActivityOrderConverter.from(activityOrderDtoList.stream().filter(a -> a.getOrderId() != null).collect(Collectors.toList())));
		return resultList;
	}


	@Override
	public List<ActivityPublishPicDTO> getActivityPublishPic(ActivityDataQuery activityDataQuery) {

		// 设置查询日期
		if (activityDataQuery.getEndDate() == null && activityDataQuery.getStartDate() == null) {
			//未设置日期范围，默认七天
			Date[] date = getDate(7);
			Date s = date[0];
			Date e = date[1];
			activityDataQuery.setStartDate(s);
			activityDataQuery.setEndDate(e);
		} else {
			activityDataQuery.setEndDate(DateUtil.endOfDay(activityDataQuery.getEndDate()));
			activityDataQuery.setStartDate(DateUtil.beginOfDay(activityDataQuery.getStartDate()));
		}

		List<Date> dateList = getDateSplit(activityDataQuery.getStartDate(), activityDataQuery.getEndDate());

		List<DataActivityCollectDTO> list = dataDao.getActivityPublishPic(activityDataQuery);

		List<ActivityPublishPicDTO> resultList = new ArrayList<>();

		for (int x = 0; x < dateList.size() - 1; x++) {
			int finalX = x;
			List<DataActivityCollectDTO> filter = list.stream().filter(a ->
					DateUtil.compare(a.getCreateTime(), dateList.get(finalX)) >= 0 &&
							DateUtil.compare(a.getCreateTime(), dateList.get(finalX + 1)) < 0
			).collect(Collectors.toList());

			ActivityPublishPicDTO dto = new ActivityPublishPicDTO();
			dto.setTime(dateList.get(finalX));

			if (CollUtil.isNotEmpty(filter)) {
				Map<String, List<DataActivityCollectDTO>> map = filter.stream().collect(Collectors.groupingBy(DataActivityCollectDTO::getType));
				List<DataActivityCollectDTO> limitList = map.get(ActivityTypeEnum.LIMIT_DISCOUNT.name());
				dto.setDiscountNum(ObjectUtil.isNull(limitList) ? 0 : limitList.size());
				List<DataActivityCollectDTO> shopList = map.get(ActivityTypeEnum.SHOP_COUPON.name());
				Integer shopNum = ObjectUtil.isNull(shopList) ? 0 : shopList.size();
				List<DataActivityCollectDTO> plartformList = map.get(ActivityTypeEnum.PLARTFORM_COUPON.name());
				Integer plartformNum = ObjectUtil.isNull(plartformList) ? 0 : plartformList.size();
				dto.setCouponNum(shopNum + plartformNum);
				List<DataActivityCollectDTO> subList = map.get(ActivityTypeEnum.SUB_MARKETING.name());
				Integer subNum = ObjectUtil.isNull(subList) ? 0 : subList.size();
				List<DataActivityCollectDTO> disList = map.get(ActivityTypeEnum.DIS_MARKETING.name());
				Integer disNum = ObjectUtil.isNull(disList) ? 0 : disList.size();
				dto.setCashNum(subNum + disNum);

				//移除已获取的数据
				list.removeAll(filter);
			}
			resultList.add(dto);
		}

		return resultList;
	}

	@Override
	public List<ActivityDealPicDTO> getActivityDealPic(ActivityDataQuery activityDataQuery) {

		// 设置查询日期
		if (activityDataQuery.getEndDate() == null && activityDataQuery.getStartDate() == null) {
			//未设置日期范围，默认七天
			Date[] date = getDate(7);
			Date s = date[0];
			Date e = date[1];
			activityDataQuery.setStartDate(s);
			activityDataQuery.setEndDate(e);
		} else {
			activityDataQuery.setEndDate(DateUtil.endOfDay(activityDataQuery.getEndDate()));
			activityDataQuery.setStartDate(DateUtil.beginOfDay(activityDataQuery.getStartDate()));
		}

		List<Date> dateList = getDateSplit(activityDataQuery.getStartDate(), activityDataQuery.getEndDate());

		List<DataActivityOrderDTO> list = dataDao.getActivityDealPic(activityDataQuery);
		list = list.stream().distinct().collect(Collectors.toList());

		List<ActivityDealPicDTO> resultList = new ArrayList<>();

		for (int x = 0; x < dateList.size(); x++) {
			int finalX = x;
			List<DataActivityOrderDTO> filter = list.stream().filter(a ->
					DateUtil.compare(a.getCreateTime(), dateList.get(finalX)) >= 0 &&
							DateUtil.compare(a.getCreateTime(), dateList.get(finalX + 1)) < 0
			).collect(Collectors.toList());

			ActivityDealPicDTO dto = new ActivityDealPicDTO();
			dto.setTime(dateList.get(finalX));

			if (CollUtil.isNotEmpty(filter)) {
				Map<String, List<DataActivityOrderDTO>> map = filter.stream().collect(Collectors.groupingBy(DataActivityOrderDTO::getActivityType));
				//限时折扣
				List<DataActivityOrderDTO> limitList = map.get(ActivityTypeEnum.LIMIT_DISCOUNT.name());
				dto.setDiscountAmount(ObjectUtil.isNull(limitList) ? BigDecimal.ZERO : limitList.stream().map(DataActivityOrderDTO::getAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));

				//店铺优惠券
				List<DataActivityOrderDTO> shopList = map.get(ActivityTypeEnum.SHOP_COUPON.name());
				BigDecimal shop = ObjectUtil.isNull(shopList) ? BigDecimal.ZERO : shopList.stream().map(DataActivityOrderDTO::getAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
				//平台优惠券
				List<DataActivityOrderDTO> plartformList = map.get(ActivityTypeEnum.PLARTFORM_COUPON.name());
				BigDecimal plartform = ObjectUtil.isNull(plartformList) ? BigDecimal.ZERO : plartformList.stream().map(DataActivityOrderDTO::getAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
				dto.setCouponAmount(shop.add(plartform));

				//满减
				List<DataActivityOrderDTO> subList = map.get(ActivityTypeEnum.SUB_MARKETING.name());
				BigDecimal sub = ObjectUtil.isNull(subList) ? BigDecimal.ZERO : subList.stream().map(DataActivityOrderDTO::getAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
				//满折
				List<DataActivityOrderDTO> disList = map.get(ActivityTypeEnum.DIS_MARKETING.name());
				BigDecimal dis = ObjectUtil.isNull(disList) ? BigDecimal.ZERO : disList.stream().map(DataActivityOrderDTO::getAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
				dto.setCashAmount(sub.add(dis));

				//移除已获取的数据
				list.removeAll(filter);
			}
			resultList.add(dto);
		}

		return resultList;
	}

	@Override
	public List<ActivityCollectPage> getActivityPage(ActivityDataQuery activityDataQuery) {

		// 设置查询日期
		if (activityDataQuery.getEndDate() == null && activityDataQuery.getStartDate() == null) {
			//未设置日期范围，默认七天
			Date[] date = getDate(7);
			Date s = date[0];
			Date e = date[1];
			activityDataQuery.setStartDate(s);
			activityDataQuery.setEndDate(e);
		} else {
			activityDataQuery.setEndDate(DateUtil.endOfDay(activityDataQuery.getEndDate()));
			activityDataQuery.setStartDate(DateUtil.beginOfDay(activityDataQuery.getStartDate()));
		}

		List<DataActivityCollectDTO> list = dataDao.getActivityPublishPic(activityDataQuery);
		List<ActivityCollectPage> resultList = new ArrayList<>();

		//根据类型分组
		Map<String, List<DataActivityCollectDTO>> map = list.stream().collect(Collectors.groupingBy(DataActivityCollectDTO::getType));

		//限时折扣
		List<DataActivityCollectDTO> limitList = map.get(LIMIT_DISCOUNT.name());
		ActivityCollectPage limitDTO = getActivityCollectPageDTO(limitList);
		limitDTO.setActivityType("限时折扣");
		limitDTO.setType(2);
		if (activityDataQuery.getShopId() != null && CollUtil.isNotEmpty(limitList)) {
			List<Long> groupId = limitList.stream().map(DataActivityCollectDTO::getActivityId).distinct().collect(Collectors.toList());
			activityDataQuery.setIds(groupId);
		}
		activityDataQuery.setType(LIMIT_DISCOUNT.name());
		List<DataActivityViewDTO> limitView = dataDao.queryActivityView(activityDataQuery);
		if (CollUtil.isNotEmpty(limitView)) {
			List<String> limitPeople = new ArrayList<>();
			limitView.forEach(a -> {
				String x = a.getUserId() + a.getActivityId() + DateUtil.format(a.getCreateTime(), DatePattern.NORM_DATE_PATTERN);
				limitPeople.add(x);
			});
			limitDTO.setTotalView(limitView.size());
			limitDTO.setTotalViewPeople((int) limitPeople.stream().distinct().count());
		} else {
			limitDTO.setTotalView(0);
			limitDTO.setTotalViewPeople(0);
		}
		if (limitDTO.getTotalViewPeople() == 0) {
			limitDTO.setInversionRate(BigDecimal.ZERO);
		} else {
			limitDTO.setInversionRate(BigDecimal.valueOf(limitDTO.getDealUserNum()).divide(BigDecimal.valueOf(limitDTO.getTotalViewPeople()), 4, RoundingMode.HALF_UP));
		}
		resultList.add(limitDTO);

		//店铺优惠券
		List<DataActivityCollectDTO> shopList = map.get(ActivityTypeEnum.SHOP_COUPON.name());
		ActivityCollectPage shopDTO = getActivityCollectPageDTO(shopList);
		shopDTO.setActivityType("店铺优惠券");
		shopDTO.setType(6);
		if (activityDataQuery.getShopId() != null && CollUtil.isNotEmpty(shopList)) {
			List<Long> groupId = shopList.stream().map(DataActivityCollectDTO::getActivityId).distinct().collect(Collectors.toList());
			activityDataQuery.setIds(groupId);
		}
		activityDataQuery.setType(SHOP_COUPON.name());
		List<DataActivityViewDTO> shopCouponView = dataDao.queryActivityView(activityDataQuery);
		if (CollUtil.isNotEmpty(shopCouponView)) {
			List<String> shopCouponPeople = new ArrayList<>();
			shopCouponView.forEach(a -> {
				String x = a.getUserId() + a.getActivityId() + DateUtil.format(a.getCreateTime(), DatePattern.NORM_DATE_PATTERN);
				shopCouponPeople.add(x);
			});
			shopDTO.setTotalView(shopCouponView.size());
			shopDTO.setTotalViewPeople((int) shopCouponPeople.stream().distinct().count());
		} else {
			shopDTO.setTotalView(0);
			shopDTO.setTotalViewPeople(0);
		}
		if (shopDTO.getTotalViewPeople() == 0) {
			shopDTO.setInversionRate(BigDecimal.ZERO);
		} else {
			shopDTO.setInversionRate(BigDecimal.valueOf(shopDTO.getDealUserNum()).divide(BigDecimal.valueOf(shopDTO.getTotalViewPeople()), 4, RoundingMode.HALF_UP));
		}
		resultList.add(shopDTO);

		if (activityDataQuery.getShopId() == null) {
			//平台优惠券
			List<DataActivityCollectDTO> plartformList = map.get(ActivityTypeEnum.PLARTFORM_COUPON.name());
			ActivityCollectPage plartformDTO = getActivityCollectPageDTO(plartformList);
			plartformDTO.setActivityType("平台优惠券");
			plartformDTO.setType(7);
			if (activityDataQuery.getShopId() != null && CollUtil.isNotEmpty(plartformList)) {
				List<Long> groupId = plartformList.stream().map(DataActivityCollectDTO::getActivityId).distinct().collect(Collectors.toList());
				activityDataQuery.setIds(groupId);
			}
			activityDataQuery.setType(PLARTFORM_COUPON.name());
			List<DataActivityViewDTO> plartformView = dataDao.queryActivityView(activityDataQuery);
			if (CollUtil.isNotEmpty(plartformView)) {
				List<String> plartformPeople = new ArrayList<>();
				plartformView.forEach(a -> {
					String x = a.getUserId() + a.getActivityId() + DateUtil.format(a.getCreateTime(), DatePattern.NORM_DATE_PATTERN);
					plartformPeople.add(x);
				});
				plartformDTO.setTotalView(plartformView.size());
				plartformDTO.setTotalViewPeople((int) plartformPeople.stream().distinct().count());
			} else {
				plartformDTO.setTotalView(0);
				plartformDTO.setTotalViewPeople(0);
			}
			if (plartformDTO.getTotalViewPeople() == 0) {
				plartformDTO.setInversionRate(BigDecimal.ZERO);
			} else {
				plartformDTO.setInversionRate(BigDecimal.valueOf(plartformDTO.getDealUserNum()).divide(BigDecimal.valueOf(plartformDTO.getTotalViewPeople()), 4, RoundingMode.HALF_UP));
			}
			resultList.add(plartformDTO);
		}

		//满减
		List<DataActivityCollectDTO> subList = map.get(ActivityTypeEnum.SUB_MARKETING.name());
		ActivityCollectPage subDTO = getActivityCollectPageDTO(subList);
		subDTO.setActivityType("满减");
		subDTO.setType(0);
		if (activityDataQuery.getShopId() != null && CollUtil.isNotEmpty(subList)) {
			List<Long> groupId = subList.stream().map(DataActivityCollectDTO::getActivityId).distinct().collect(Collectors.toList());
			activityDataQuery.setIds(groupId);
		}
		activityDataQuery.setType(SUB_MARKETING.name());
		List<DataActivityViewDTO> subView = dataDao.queryActivityView(activityDataQuery);
		if (CollUtil.isNotEmpty(subView)) {
			List<String> subPeople = new ArrayList<>();
			subView.forEach(a -> {
				String x = a.getUserId() + a.getActivityId() + DateUtil.format(a.getCreateTime(), DatePattern.NORM_DATE_PATTERN);
				subPeople.add(x);
			});
			subDTO.setTotalView(subView.size());
			subDTO.setTotalViewPeople((int) subPeople.stream().distinct().count());
		} else {
			subDTO.setTotalView(0);
			subDTO.setTotalViewPeople(0);
		}
		if (subDTO.getTotalViewPeople() == 0) {
			subDTO.setInversionRate(BigDecimal.ZERO);
		} else {
			subDTO.setInversionRate(BigDecimal.valueOf(subDTO.getDealUserNum()).divide(BigDecimal.valueOf(subDTO.getTotalViewPeople()), 4, RoundingMode.HALF_UP));
		}
		resultList.add(subDTO);

		//满折
		List<DataActivityCollectDTO> disList = map.get(ActivityTypeEnum.DIS_MARKETING.name());
		ActivityCollectPage disDTO = getActivityCollectPageDTO(disList);
		disDTO.setActivityType("满折");
		disDTO.setType(1);
		if (activityDataQuery.getShopId() != null && CollUtil.isNotEmpty(disList)) {
			List<Long> groupId = disList.stream().map(DataActivityCollectDTO::getActivityId).distinct().collect(Collectors.toList());
			activityDataQuery.setIds(groupId);
		}
		activityDataQuery.setType(DIS_MARKETING.name());
		List<DataActivityViewDTO> disView = dataDao.queryActivityView(activityDataQuery);
		if (CollUtil.isNotEmpty(disView)) {
			List<String> disPeople = new ArrayList<>();
			disView.forEach(a -> {
				String x = a.getUserId() + a.getActivityId() + DateUtil.format(a.getCreateTime(), DatePattern.NORM_DATE_PATTERN);
				disPeople.add(x);
			});
			disDTO.setTotalView(disView.size());
			disDTO.setTotalViewPeople((int) disPeople.stream().distinct().count());
		} else {
			disDTO.setTotalView(0);
			disDTO.setTotalViewPeople(0);
		}
		if (disDTO.getTotalViewPeople() == 0) {
			disDTO.setInversionRate(BigDecimal.ZERO);
		} else {
			disDTO.setInversionRate(BigDecimal.valueOf(disDTO.getDealUserNum()).divide(BigDecimal.valueOf(disDTO.getTotalViewPeople()), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
		}
		resultList.add(disDTO);

		if (activityDataQuery.getOrder() != null && activityDataQuery.getProp() != null) {
			if (activityDataQuery.getOrder()) {
				switch (activityDataQuery.getProp()) {
					case "usage":
						resultList = resultList.stream().sorted((Comparator.comparing(ActivityCollectPage::getUsage))).collect(Collectors.toList());
						break;
					case "amount":
						resultList = resultList.stream().sorted((Comparator.comparing(ActivityCollectPage::getAmount))).collect(Collectors.toList());
						break;
					case "count":
						resultList = resultList.stream().sorted((Comparator.comparing(ActivityCollectPage::getCount))).collect(Collectors.toList());
						break;
					case "dealUserNum":
						resultList = resultList.stream().sorted((Comparator.comparing(ActivityCollectPage::getDealUserNum))).collect(Collectors.toList());
						break;
					case "newUserNum":
						resultList = resultList.stream().sorted((Comparator.comparing(ActivityCollectPage::getNewUserNum))).collect(Collectors.toList());
						break;
					case "oldUserNum":
						resultList = resultList.stream().sorted((Comparator.comparing(ActivityCollectPage::getOldUserNum))).collect(Collectors.toList());
						break;
					case "totalView":
						resultList = resultList.stream().sorted((Comparator.comparing(ActivityCollectPage::getTotalView))).collect(Collectors.toList());
						break;
					case "totalViewPeople":
						resultList = resultList.stream().sorted((Comparator.comparing(ActivityCollectPage::getTotalViewPeople))).collect(Collectors.toList());
						break;
					case "inversionRate":
						resultList = resultList.stream().sorted((Comparator.comparing(ActivityCollectPage::getInversionRate))).collect(Collectors.toList());
						break;
					default:

				}
			} else {
				switch (activityDataQuery.getProp()) {
					case "usage":
						resultList = resultList.stream().sorted((Comparator.comparing(ActivityCollectPage::getUsage)).reversed()).collect(Collectors.toList());
						break;
					case "amount":
						resultList = resultList.stream().sorted((Comparator.comparing(ActivityCollectPage::getAmount)).reversed()).collect(Collectors.toList());
						break;
					case "count":
						resultList = resultList.stream().sorted((Comparator.comparing(ActivityCollectPage::getCount)).reversed()).collect(Collectors.toList());
						break;
					case "dealUserNum":
						resultList = resultList.stream().sorted((Comparator.comparing(ActivityCollectPage::getDealUserNum)).reversed()).collect(Collectors.toList());
						break;
					case "newUserNum":
						resultList = resultList.stream().sorted((Comparator.comparing(ActivityCollectPage::getNewUserNum)).reversed()).collect(Collectors.toList());
						break;
					case "oldUserNum":
						resultList = resultList.stream().sorted((Comparator.comparing(ActivityCollectPage::getOldUserNum)).reversed()).collect(Collectors.toList());
						break;
					case "totalView":
						resultList = resultList.stream().sorted((Comparator.comparing(ActivityCollectPage::getTotalView)).reversed()).collect(Collectors.toList());
						break;
					case "totalViewPeople":
						resultList = resultList.stream().sorted((Comparator.comparing(ActivityCollectPage::getTotalViewPeople)).reversed()).collect(Collectors.toList());
						break;
					case "inversionRate":
						resultList = resultList.stream().sorted((Comparator.comparing(ActivityCollectPage::getInversionRate)).reversed()).collect(Collectors.toList());
						break;
					default:

				}
			}
		}

		return resultList;

	}

	@Override
	public ActivityCollectDetail getActivityDetail(ActivityDataQuery activityDataQuery) {

		// 设置查询日期
		if (activityDataQuery.getEndDate() == null && activityDataQuery.getStartDate() == null) {
			//未设置日期范围，默认七天
			Date[] date = getDate(7);
			Date s = date[0];
			Date e = date[1];
			activityDataQuery.setStartDate(s);
			activityDataQuery.setEndDate(e);
		} else {
			activityDataQuery.setEndDate(DateUtil.endOfDay(activityDataQuery.getEndDate()));
			activityDataQuery.setStartDate(DateUtil.beginOfDay(activityDataQuery.getStartDate()));
		}

		List<DataActivityCollectDTO> list = dataDao.getActivityPublishPic(activityDataQuery);

		ActivityCollectDetail result = new ActivityCollectDetail();
		result.setTotalUsage(list.size());

		//根据类型分组
		Map<String, List<DataActivityCollectDTO>> map = list.stream().collect(Collectors.groupingBy(DataActivityCollectDTO::getType));

		//限时折扣
		List<DataActivityCollectDTO> limitList = map.get(ActivityTypeEnum.LIMIT_DISCOUNT.name());
		Integer[] limitDetail = {0, 0};
		if (CollUtil.isNotEmpty(limitList)) {
			limitDetail = getActivityDetail(limitList, ActivityTypeEnum.LIMIT_DISCOUNT);
		}

		//店铺优惠券
		List<DataActivityCollectDTO> shopList = map.get(ActivityTypeEnum.SHOP_COUPON.name());
		Integer[] shopDetail = {0, 0};
		if (CollUtil.isNotEmpty(shopList)) {
			shopDetail = getActivityDetail(shopList, ActivityTypeEnum.SHOP_COUPON);
		}

		//满减
		List<DataActivityCollectDTO> subList = map.get(ActivityTypeEnum.SUB_MARKETING.name());
		Integer[] subDetail = {0, 0};
		if (CollUtil.isNotEmpty(subList)) {
			subDetail = getActivityDetail(subList, ActivityTypeEnum.SUB_MARKETING);
		}

		//满折
		List<DataActivityCollectDTO> disList = map.get(ActivityTypeEnum.DIS_MARKETING.name());
		Integer[] disDetail = {0, 0};
		if (CollUtil.isNotEmpty(disList)) {
			disDetail = getActivityDetail(disList, ActivityTypeEnum.DIS_MARKETING);
		}

		List<ActivityPayDataDTO> payData = dataDao.getActivityPayData(activityDataQuery);
		if (payData != null) {
			BigDecimal totalPrice = payData.stream().map(ActivityPayDataDTO::getTotalPrice).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal actualTotalPrice = payData.stream().map(ActivityPayDataDTO::getActualTotalPrice).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
			result.setPayAmount(totalPrice.subtract(actualTotalPrice));

			List<BigDecimal> allPay = dataDao.getPayData(activityDataQuery);
			BigDecimal allPrice = allPay.stream().reduce(BigDecimal.ZERO, BigDecimal::add);

			if (!Objects.equals(allPrice, BigDecimal.ZERO)) {
				result.setPayPercentage(actualTotalPrice.divide(allPrice, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
			} else {
				result.setPayPercentage(BigDecimal.ZERO);
			}

			if (payData.size() != 0) {
				int sum = payData.stream().mapToInt(ActivityPayDataDTO::getProductQuantity).sum();
				result.setRate(BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(payData.size()), 4, RoundingMode.HALF_UP));
			} else {
				result.setRate(BigDecimal.ZERO);
			}
		} else {
			result.setPayAmount(BigDecimal.ZERO);
			result.setPayPercentage(BigDecimal.ZERO);
			result.setRate(BigDecimal.ZERO);
		}


		return result;
	}

	@Override
	public void saveActivityViewData(ActivityViewDTO activityViewDTO) {

		if (CollUtil.isEmpty(activityViewDTO.getActivityList()) || activityViewDTO.getUserId() == null) {
			return;
		}

		for (String[] a : activityViewDTO.getActivityList()) {

			long activityId = Long.parseLong(a[0]);
			String type = a[1];
			Long userId = activityViewDTO.getUserId();

			DataActivityViewDTO dataActivityViewDTO = new DataActivityViewDTO();
			dataActivityViewDTO.setActivityId(activityId);
			dataActivityViewDTO.setType(type);
			dataActivityViewDTO.setCreateTime(DateUtil.date());
			dataActivityViewDTO.setUserId(userId);
			DataActivityView entity = dataActivityViewConverter.from(dataActivityViewDTO);
			dataActivityViewDao.save(entity);

		}

	}


	@Override
	public R<Void> dataUserAmountJobHandle() {

		DataUserAmountDTO lastedDataDate = this.getLastedDataDate();
		List<DataUserAmountDTO> userAmountNewData = null;
		if (lastedDataDate != null) {
			long between = DateUtil.between(lastedDataDate.getCreateTime(), DateUtil.beginOfDay(new Date()), DateUnit.DAY);
			if (between >= 2) {
				Integer num = Math.toIntExact(between > 30 ? 30 : between);
				userAmountNewData = this.getUserAmountNewData(num);
			}
		} else {
			log.info("第一次进行统计，如果用户详细表有数据，获取最近三十天的数据");
			if (this.confirmUserHaveData()) {
				userAmountNewData = this.getUserAmountNewData(30);
			}
		}
		//有数据，进行保存
		if (userAmountNewData != null) {
			List<DataUserAmount> from = dataUserAmountConverter.from(userAmountNewData);
			userAmountDao.save(from);
		}
		return R.ok();
	}

	private ActivityCollectPage getActivityCollectPageDTO(List<DataActivityCollectDTO> list) {

		if (CollUtil.isEmpty(list)) {
			return new ActivityCollectPage();
		}
		List<String> idTypes = new ArrayList<>();
		list.forEach(a -> {
			String idType = a.getActivityId() + a.getType();
			idTypes.add(idType);
		});

		ActivityDataQuery activityDataQuery = new ActivityDataQuery();
		activityDataQuery.setIdType(idTypes);
		List<DataActivityOrderDTO> activityOrderDtoList = dataDao.getActivityDealPic(activityDataQuery);
		if (CollUtil.isEmpty(activityOrderDtoList)) {
			ActivityCollectPage activityCollectPage = new ActivityCollectPage();
			activityCollectPage.setUsage(list.size());
			return activityCollectPage;
		}
		activityOrderDtoList = activityOrderDtoList.stream().distinct().collect(Collectors.toList());
		List<Long> userIds = activityOrderDtoList.stream().map(DataActivityOrderDTO::getUserId).distinct().collect(Collectors.toList());

		if (ObjectUtil.isNotNull(list)) {
			ActivityCollectPage resultDTO = new ActivityCollectPage();
			resultDTO.setDealUserNum(userIds.size());
			Integer num = 0;
			for (int x = 0; x < activityOrderDtoList.size(); x++) {
				DataActivityOrderDTO dto = activityOrderDtoList.get(x);
				resultDTO.setAmount(resultDTO.getAmount().add(dto.getAmount()));
				resultDTO.setCount(resultDTO.getCount() + dto.getBasketCount());
				if (userIds.contains(dto.getUserId())) {
					Boolean aBoolean = dataDao.queryIfNewCustomer(dto.getUserId(), dto.getCreateTime());
					if (aBoolean) {
						num++;
					}
					userIds.remove(dto.getUserId());
				}
			}
			resultDTO.setNewUserNum(num);
			int old = resultDTO.getDealUserNum() - num;
			resultDTO.setOldUserNum(Math.max(old, 0));
			resultDTO.setUsage(list.size());
			return resultDTO;
		} else {
			ActivityCollectPage activityCollectPage = new ActivityCollectPage();
			activityCollectPage.setUsage(list.size());
			return activityCollectPage;
		}
	}

	private Integer[] getActivityDetail(List<DataActivityCollectDTO> list, ActivityTypeEnum activityTypeEnum) {

		if (Objects.equals(ActivityTypeEnum.SHOP_COUPON, activityTypeEnum) || Objects.equals(ActivityTypeEnum.PLARTFORM_COUPON, activityTypeEnum)) {
			long effect = list.stream().filter(a -> a.getActivityStatus() > -1).count();
			long unEffect = list.stream().filter(a -> a.getActivityStatus() < 0).count();
			Integer[] integers = new Integer[2];
			integers[0] = Math.toIntExact(effect);
			integers[1] = Math.toIntExact(unEffect);
			return integers;
		}

		if (Objects.equals(ActivityTypeEnum.LIMIT_DISCOUNT, activityTypeEnum) || Objects.equals(ActivityTypeEnum.SUB_MARKETING, activityTypeEnum) || Objects.equals(ActivityTypeEnum.DIS_MARKETING, activityTypeEnum)) {
			long effect = list.stream().filter(a -> a.getActivityStatus() > -1 && a.getActivityStatus() < 4).count();
			long unEffect = list.stream().filter(a -> a.getActivityStatus() > 3 || a.getActivityStatus() < 0).count();
			Integer[] integers = new Integer[2];
			integers[0] = Math.toIntExact(effect);
			integers[1] = Math.toIntExact(unEffect);
			return integers;
		}

		return null;
	}


	/**
	 * 获取小程序和APP的数据
	 *
	 * @return
	 */
	private UserDataViewsDTO getMiniOrAppUserViewData(Date startDate, Date endDate) {
		UserDataViewsDTO dto = new UserDataViewsDTO();
		//新增用户数量
		Date s = startDate;
		Date e = endDate;
		if (startDate == null && endDate == null) {
			Date[] date = getDate(1);
			s = date[0];
			e = date[1];
		}
		BaiduView integers = baiduViewDao.getAllByArchiveTime(null, e);
		dto.setUserViews(integers.getMiniUv());

		//日增量
		BaiduView yesterday = baiduViewDao.getAllByArchiveTime(s, e);

		Date s1 = DateUtil.offset(s, DateField.DAY_OF_YEAR, -1);
		Date e1 = DateUtil.offset(e, DateField.DAY_OF_YEAR, -1);
		BaiduView num = baiduViewDao.getAllByArchiveTime(s1, e1);
		Integer dayNum = yesterday.getMiniUv() - num.getMiniUv();
		dto.setUserViewsByDay(dayNum);

		//周增量
		Date[] date1 = getDate(7);
		Date s2 = date1[0];
		Date e2 = date1[1];
		//本周增量
		BaiduView dataWeek = baiduViewDao.getAllByArchiveTime(s2, e2);
		//上周增量
		Date s7 = DateUtil.offset(s2, DateField.DAY_OF_YEAR, -7);
		Date e7 = DateUtil.offset(e2, DateField.DAY_OF_YEAR, -7);
		BaiduView dataWeek1 = baiduViewDao.getAllByArchiveTime(s7, e7);
		Integer weekNum = dataWeek.getMiniUv() - dataWeek1.getMiniUv();
		dto.setUserViewsByWeek(weekNum);

		//月增量
		Date[] date2 = getDate(30);
		Date s3 = date2[0];
		Date e3 = date2[1];
		//本月增量
		BaiduView dataMonth = baiduViewDao.getAllByArchiveTime(s3, e3);
		//上月增量
		DateTime s30 = DateUtil.offset(s3, DateField.DAY_OF_YEAR, -30);
		DateTime e30 = DateUtil.offset(e3, DateField.DAY_OF_YEAR, -30);
		BaiduView dataMonth1 = baiduViewDao.getAllByArchiveTime(s30, e30);
		Integer monthNum = dataMonth.getMiniUv() - dataMonth1.getMiniUv();
		dto.setUserViewsByMonth(monthNum);

		return dto;
	}

	private UserDataViewsDTO getH5UserViewData() {
		return getH5UserViewData(null, null);
	}

	private UserDataViewsDTO getH5UserViewData(Date startDate, Date endDate) {
		Date s = startDate;
		Date e = endDate;
		if (startDate == null && endDate == null) {
			Date[] date = getDate(1);
			s = date[0];
			e = date[1];
		}
		UserDataViewsDTO dto = new UserDataViewsDTO();

		BaiduView baiduViewTotal = baiduViewDao.getAllByArchiveTime(null, e);

		//新增用户数量
		dto.setUserViews(baiduViewTotal.getH5Uv());

		//日增量
		BaiduView yesterday = baiduViewDao.getAllByArchiveTime(s, e);

		Date s1 = DateUtil.offset(s, DateField.DAY_OF_YEAR, -1);
		Date e1 = DateUtil.offset(e, DateField.DAY_OF_YEAR, -1);
		BaiduView num = baiduViewDao.getAllByArchiveTime(s1, e1);
		Integer dayNum = yesterday.getH5Uv() - num.getH5Uv();
		dto.setUserViewsByDay(dayNum);

		//周增量
		Date[] date1 = getDate(7);
		Date s2 = date1[0];
		Date e2 = date1[1];
		//本周增量
		BaiduView dataWeek = baiduViewDao.getAllByArchiveTime(s2, e2);
		//上周增量
		Date s7 = DateUtil.offset(s2, DateField.DAY_OF_YEAR, -7);
		Date e7 = DateUtil.offset(e2, DateField.DAY_OF_YEAR, -7);
		BaiduView dataWeek1 = baiduViewDao.getAllByArchiveTime(s7, e7);
		Integer weekNum = dataWeek.getH5Uv() - dataWeek1.getH5Uv();
		dto.setUserViewsByWeek(weekNum);

		//月增量
		Date[] date2 = getDate(30);
		Date s3 = date2[0];
		Date e3 = date2[1];
		//本月增量
		BaiduView dataMonth = baiduViewDao.getAllByArchiveTime(s3, e3);
		//上月增量
		DateTime s30 = DateUtil.offset(s3, DateField.DAY_OF_YEAR, -30);
		DateTime e30 = DateUtil.offset(e3, DateField.DAY_OF_YEAR, -30);
		BaiduView dataMonth1 = baiduViewDao.getAllByArchiveTime(s30, e30);
		Integer monthNum = dataMonth.getH5Uv() - dataMonth1.getH5Uv();
		dto.setUserViewsByMonth(monthNum);

		return dto;
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
	 * 百度统计，根据value返回Pv、UV
	 */
	private Integer[] baiDuTongJi(String startDate, String endDate) {
		JSONObject jsonObject = new JSONObject();
		JSONObject header = new JSONObject();
		header.put("username", baiDuPropertiesDTO.getUserName());
		header.put("password", baiDuPropertiesDTO.getPassword());
		header.put("token", baiDuPropertiesDTO.getToken());
		header.put("account_type", 1);
		JSONObject body = new JSONObject();
		body.put("site_id", baiDuPropertiesDTO.getSiteId());
		body.put("start_date", startDate);
		body.put("end_date", endDate);
		body.put("metrics", "out_pv_count,visitor_count");
		body.put("method", "visit/landingpage/a");
		jsonObject.put("header", header);
		jsonObject.put("body", body);
		log.info("百度统计请求体：{}", jsonObject.toJSONString());
		String postValue = HttpUtil.post(baiDuPropertiesDTO.getTongJiUrl(), jsonObject.toJSONString());
		JSONObject json = JSONObject.parseObject(postValue);
		log.info("百度统计返回Json：{}", json.toString());
		Integer[] count = new Integer[2];
		try {
			count[0] = json.getJSONObject("body").getJSONArray("data").getJSONObject(0).getJSONObject("result").getJSONArray("sum").getJSONArray(0).getInteger(0);
			count[1] = json.getJSONObject("body").getJSONArray("data").getJSONObject(0).getJSONObject("result").getJSONArray("sum").getJSONArray(0).getInteger(1);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return count;
	}

	/**
	 * 百度移动统计，根据value返回Pv、UV
	 */
	private Integer[] baiDuMobileTongJi(String startDate, String endDate, String source) {
		R<BaiDuMobileStatisticsSettingDTO> result = sysParamsApi.getConfigDtoByParamName(SysParamNameEnum.BAIDU_MOBILE_STATISTICS.name(), BaiDuMobileStatisticsSettingDTO.class);
		if (!result.getSuccess()) {
			log.info("获取百度移动统计配置失败~");
			return new Integer[]{0, 0};
		}

		// 处理拆分为微服务后，强转失败问题
		ObjectMapper mapper = new ObjectMapper();

		BaiDuMobileStatisticsSettingDTO settingDTO = mapper.convertValue(result.getData(), BaiDuMobileStatisticsSettingDTO.class);

		JSONObject body = new JSONObject();
		body.put("access_token", settingDTO.getAccessToken());
		body.put("key", baiDuPropertiesDTO.getMiniAppKey());
		body.put("method", "newuser/a");
		body.put("metrics", "session_count,user_count");
		body.put("start-date", startDate);
		body.put("end-date", endDate);
		log.info("百度移动统计请求体：{}", body.toJSONString());
		String postValue = HttpUtil.get(baiDuPropertiesDTO.getMobileTongJiUrl(), body);
		JSONObject json = JSONObject.parseObject(postValue);
		log.info("百度移动统计返回Json：{}", json.toString());
		Integer[] count = new Integer[2];
		try {
			count[0] = json.getJSONObject("result").getJSONArray("sum").getJSONArray(0).getInteger(0);
			count[1] = json.getJSONObject("result").getJSONArray("sum").getJSONArray(0).getInteger(1);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return count;
	}

	/**
	 * 将map转换成url
	 *
	 * @param map
	 * @return
	 */
	public static String getUrlParamsByMap(Map<String, Object> map) {
		if (map == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("?");
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			sb.append(entry.getKey() + "=" + entry.getValue());
			sb.append("&");
		}
		String s = sb.toString();
		if (s.endsWith("&")) {
			s = StringUtils.substringBeforeLast(s, "&");
		}
		return s;
	}

	/**
	 * 将时间范围分成十份，不能均分时添加随机元素，不足十天时根据一天分割。
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private List<Date> getDateAvg(Date startDate, Date endDate) {
		Integer between = Math.toIntExact(DateUtil.between(startDate, endDate, DateUnit.DAY));
		List<Integer> list = new ArrayList<>();
		Integer avg = null;
		if (between < 10) {
			for (int x = 1; x <= between; x++) {
				list.add(1);
			}
			avg = 1;
		} else {
			double v = between / 9.0;
			int i = between % 9;
			Integer floor = (int) Math.floor(v);
			for (int x = 1; x < 10; x++) {
				list.add(floor);
			}
			Integer min = floor;
			avg = floor;
			while (i > 0) {
				Integer random = (int) Math.floor(Math.random() * 9);
				Integer now = list.get(random);
				if (now > min) {
					continue;
				}
				list.set(random, now + 1);
				i--;
			}
		}
		List<Date> dateList = new ArrayList<>();
		dateList.add(startDate);
		AtomicReference<Date> temp = new AtomicReference<>(startDate);
		list.forEach(a -> {
			Date offset = DateUtil.offset(temp.get(), DateField.DAY_OF_YEAR, a);
			temp.set(offset);
			dateList.add(offset);
		});
		dateList.add(DateUtil.offset(temp.get(), DateField.DAY_OF_YEAR, avg));
		return dateList;
	}

	/**
	 * 将日期分割成以天为单位的list
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private List<Date> getDateSplit(Date startDate, Date endDate) {
		Integer between = Math.toIntExact(DateUtil.between(startDate, endDate, DateUnit.DAY)) + 1;
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
