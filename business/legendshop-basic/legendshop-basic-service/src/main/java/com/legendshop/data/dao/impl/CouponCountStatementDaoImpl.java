/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dao.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.QueryMap;
import com.legendshop.activity.dto.MarketingDataTrendDTO;
import com.legendshop.common.datasource.NonTable;
import com.legendshop.data.dao.CouponCountStatementDao;
import com.legendshop.data.dto.ActivityCouponCountQuery;
import com.legendshop.data.dto.MarketingAmountStatisticsDTO;
import com.legendshop.data.dto.MarketingUserStatisticsDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 商家和平台营销数据统计报表
 *
 * @author legendshop
 */
@Repository
public class CouponCountStatementDaoImpl extends GenericDaoImpl<NonTable, Long> implements CouponCountStatementDao {

	@Override
	public MarketingAmountStatisticsDTO getMarketingAmount(ActivityCouponCountQuery query) {
		QueryMap map = new QueryMap();
		map.put("couponId", query.getCouponId());
		map.put("shopId", query.getShopId());
		map.put("source", query.getSource());
		map.put("beginTime", query.getBeginTime());
		if (ObjectUtil.isNotEmpty(query.getEndTime())) {
			map.put("endTime", DateUtil.endOfDay(query.getEndTime()));
		}
		SQLOperation operation = this.getSQLAndParams("CouponCountStatement.getMarketingAmount", map);
		return get(operation.getSql(), MarketingAmountStatisticsDTO.class, operation.getParams());
	}

	@Override
	public MarketingAmountStatisticsDTO getTransactionAmount(ActivityCouponCountQuery query) {
		QueryMap map = new QueryMap();
		map.put("couponId", query.getCouponId());
		map.put("source", query.getSource());
		map.put("beginTime", query.getBeginTime());
		if (ObjectUtil.isNotEmpty(query.getEndTime())) {
			map.put("endTime", DateUtil.endOfDay(query.getEndTime()));
		}
		SQLOperation operation = this.getSQLAndParams("CouponCountStatement.getTransactionAmount", map);
		return get(operation.getSql(), MarketingAmountStatisticsDTO.class, operation.getParams());
	}

	@Override
	public MarketingUserStatisticsDTO getReceivedCount(ActivityCouponCountQuery query) {
		QueryMap map = new QueryMap();
		map.put("shopId", query.getShopId());
		map.put("couponId", query.getCouponId());
		map.put("source", query.getSource());
		map.put("beginTime", query.getBeginTime());
		if (ObjectUtil.isNotEmpty(query.getEndTime())) {
			map.put("endTime", DateUtil.endOfDay(query.getEndTime()));
		}
		SQLOperation operation = this.getSQLAndParams("CouponCountStatement.getReceivedCount", map);
		return get(operation.getSql(), MarketingUserStatisticsDTO.class, operation.getParams());
	}

	@Override
	public MarketingUserStatisticsDTO getTotalReceivedCount(ActivityCouponCountQuery query) {
		QueryMap map = new QueryMap();
		map.put("shopId", query.getShopId());
		map.put("couponId", query.getCouponId());
		map.put("source", query.getSource());
		if (ObjectUtil.isNotEmpty(query.getEndTime())) {
			map.put("endTime", DateUtil.endOfDay(query.getEndTime()));
		}
		SQLOperation operation = this.getSQLAndParams("CouponCountStatement.getReceivedCount", map);
		return get(operation.getSql(), MarketingUserStatisticsDTO.class, operation.getParams());
	}

	@Override
	public MarketingUserStatisticsDTO getOrderCount(ActivityCouponCountQuery query) {
		QueryMap map = new QueryMap();
		map.put("couponId", query.getCouponId());
		map.put("shopId", query.getShopId());
		map.put("source", query.getSource());
		map.put("beginTime", query.getBeginTime());
		if (ObjectUtil.isNotEmpty(query.getEndTime())) {
			map.put("endTime", DateUtil.endOfDay(query.getEndTime()));
		}
		SQLOperation operation = this.getSQLAndParams("CouponCountStatement.getOrderCount", map);
		return get(operation.getSql(), MarketingUserStatisticsDTO.class, operation.getParams());
	}

	@Override
	public Long getOldOrderUserCount(ActivityCouponCountQuery query) {
		QueryMap map = new QueryMap();
		map.put("couponId", query.getCouponId());
		map.put("shopId", query.getShopId());
		map.put("source", query.getSource());
		map.put("beginTime", query.getBeginTime());
		if (ObjectUtil.isNotEmpty(query.getEndTime())) {
			map.put("endTime", DateUtil.endOfDay(query.getEndTime()));
		}
		SQLOperation operation = this.getSQLAndParams("CouponCountStatement.getOldOrderUserCount", map);
		Long count = get(operation.getSql(), Long.class, operation.getParams());
		return Optional.ofNullable(count).orElse(0L);
	}

	@Override
	public Long getOrderUserCount(ActivityCouponCountQuery query) {
		QueryMap map = new QueryMap();
		map.put("couponId", query.getCouponId());
		map.put("shopId", query.getShopId());
		map.put("source", query.getSource());
		map.put("beginTime", query.getBeginTime());
		if (ObjectUtil.isNotEmpty(query.getEndTime())) {
			map.put("endTime", DateUtil.endOfDay(query.getEndTime()));
		}
		SQLOperation operation = this.getSQLAndParams("CouponCountStatement.getOrderUserCount", map);
		return get(operation.getSql(), Long.class, operation.getParams());
	}

	@Override
	public List<MarketingDataTrendDTO> queryPlatformNewVisitCount(ActivityCouponCountQuery query) {
		QueryMap map = new QueryMap();
		map.put("couponId", query.getCouponId());
		map.put("shopId", query.getShopId());
		map.put("source", query.getSource());
		map.put("beginTime", query.getBeginTime());
		map.put("endTime", query.getEndTime());
		SQLOperation operation = this.getSQLAndParams("CouponCountStatement.queryPlatformNewVisitCount", map);
		List<MarketingDataTrendDTO> dataTrendDTOList = query(operation.getSql(), MarketingDataTrendDTO.class, operation.getParams());
		if (CollectionUtil.isNotEmpty(dataTrendDTOList)) {
			return dataTrendDTOList;
		}
		ArrayList<MarketingDataTrendDTO> dataTrendDtoArrayList = new ArrayList<>();
		return dataTrendDtoArrayList;
	}

	@Override
	public List<MarketingDataTrendDTO> queryPlatformVisitCount(ActivityCouponCountQuery query) {
		QueryMap map = new QueryMap();
		map.put("couponId", query.getCouponId());
		map.put("shopId", query.getShopId());
		map.put("source", query.getSource());
		map.put("endTime", query.getEndTime());
		SQLOperation operation = this.getSQLAndParams("CouponCountStatement.queryPlatformVisitCount", map);
		List<MarketingDataTrendDTO> dataTrendDtoList = query(operation.getSql(), MarketingDataTrendDTO.class, operation.getParams());
		if (CollectionUtil.isNotEmpty(dataTrendDtoList)) {
			return dataTrendDtoList;
		}
		ArrayList<MarketingDataTrendDTO> dataTrendDtoArrayList = new ArrayList<>();
		return dataTrendDtoArrayList;
	}

	@Override
	public MarketingUserStatisticsDTO getPlatformUserOrderCount(ActivityCouponCountQuery query) {
		QueryMap map = new QueryMap();
		map.put("couponId", query.getCouponId());
		map.put("source", query.getSource());
		map.put("beginTime", query.getBeginTime());
		map.put("endTime", query.getEndTime());
		SQLOperation operation = this.getSQLAndParams("CouponCountStatement.getPlatformUserOrderCount", map);
		MarketingUserStatisticsDTO userStatisticsDTO = get(operation.getSql(), MarketingUserStatisticsDTO.class, operation.getParams());
		if (!ObjectUtil.isEmpty(userStatisticsDTO)) {
			return userStatisticsDTO;
		}
		return new MarketingUserStatisticsDTO();
	}

	@Override
	public MarketingUserStatisticsDTO getPlatformUserPayCount(ActivityCouponCountQuery query) {
		QueryMap map = new QueryMap();
		map.put("couponId", query.getCouponId());
		map.put("source", query.getSource());
		map.put("beginTime", query.getBeginTime());
		map.put("endTime", query.getEndTime());
		SQLOperation operation = this.getSQLAndParams("CouponCountStatement.getPlatformUserPayCount", map);
		MarketingUserStatisticsDTO marketingUserStatisticsDTO = get(operation.getSql(), MarketingUserStatisticsDTO.class, operation.getParams());
		if (!ObjectUtil.isEmpty(marketingUserStatisticsDTO)) {
			return marketingUserStatisticsDTO;
		}
		return new MarketingUserStatisticsDTO();
	}

	@Override
	public List<MarketingUserStatisticsDTO> queryPlatformOrderOldUserCount(ActivityCouponCountQuery query) {
		QueryMap map = new QueryMap();
		map.put("couponId", query.getCouponId());
		map.put("source", query.getSource());
		map.put("beginTime", query.getBeginTime());
		SQLOperation operation = this.getSQLAndParams("CouponCountStatement.queryPlatformOrderOldUserCount", map);
		List<MarketingUserStatisticsDTO> userStatisticsDTOList = query(operation.getSql(), MarketingUserStatisticsDTO.class, operation.getParams());
		if (!CollectionUtil.isEmpty(userStatisticsDTOList)) {
			return userStatisticsDTOList;
		}
		ArrayList<MarketingUserStatisticsDTO> statisticsDtoArrayList = new ArrayList<>();
		return statisticsDtoArrayList;
	}

	@Override
	public List<MarketingUserStatisticsDTO> queryPlatformOrderUserIdCount(ActivityCouponCountQuery query) {
		QueryMap map = new QueryMap();
		map.put("couponId", query.getCouponId());
		map.put("source", query.getSource());
		map.put("beginTime", query.getBeginTime());
		map.put("endTime", query.getEndTime());
		SQLOperation operation = this.getSQLAndParams("CouponCountStatement.queryPlatformOrderUserIdCount", map);
		List<MarketingUserStatisticsDTO> userStatisticsDTOList = query(operation.getSql(), MarketingUserStatisticsDTO.class, operation.getParams());
		if (!CollectionUtil.isEmpty(userStatisticsDTOList)) {
			return userStatisticsDTOList;
		}
		ArrayList<MarketingUserStatisticsDTO> statisticsDtoArrayList = new ArrayList<>();
		return statisticsDtoArrayList;
	}

	@Override
	public List<MarketingDataTrendDTO> queryPlatformNewAmount(ActivityCouponCountQuery query) {
		QueryMap map = new QueryMap();
		map.put("source", query.getSource());
		map.put("beginTime", query.getBeginTime());
		map.put("endTime", query.getEndTime());
		SQLOperation operation = this.getSQLAndParams("CouponCountStatement.queryPlatformNewAmount", map);
		List<MarketingDataTrendDTO> dataTrendDTOList = query(operation.getSql(), MarketingDataTrendDTO.class, operation.getParams());
		if (!CollectionUtil.isEmpty(dataTrendDTOList)) {
			return dataTrendDTOList;
		}
		ArrayList<MarketingDataTrendDTO> marketingDataTrendDtoList = new ArrayList<>();
		return marketingDataTrendDtoList;
	}

	@Override
	public List<MarketingDataTrendDTO> queryPlatformAmountList(ActivityCouponCountQuery query) {
		QueryMap map = new QueryMap();
		map.put("couponId", query.getCouponId());
		map.put("source", query.getSource());
		map.put("endTime", query.getEndTime());
		SQLOperation operation = this.getSQLAndParams("CouponCountStatement.queryPlatformAmountList", map);
		List<MarketingDataTrendDTO> dataTrendDTOList = query(operation.getSql(), MarketingDataTrendDTO.class, operation.getParams());
		if (!CollectionUtil.isEmpty(dataTrendDTOList)) {
			return dataTrendDTOList;
		}
		ArrayList<MarketingDataTrendDTO> marketingDataTrendDtoList = new ArrayList<>();
		return marketingDataTrendDtoList;
	}

	@Override
	public List<MarketingDataTrendDTO> queryPlatformNewOrderUserCount(ActivityCouponCountQuery query) {
		QueryMap map = new QueryMap();
		map.put("couponId", query.getCouponId());
		map.put("source", query.getSource());
		map.put("beginTime", query.getBeginTime());
		map.put("endTime", query.getEndTime());
		SQLOperation operation = this.getSQLAndParams("CouponCountStatement.queryPlatformNewOrderUserCount", map);
		List<MarketingDataTrendDTO> dataTrendDTOList = query(operation.getSql(), MarketingDataTrendDTO.class, operation.getParams());
		if (CollectionUtil.isEmpty(dataTrendDTOList)) {
			return dataTrendDTOList;
		}
		ArrayList<MarketingDataTrendDTO> marketingDataTrendDtoList = new ArrayList<>();
		return marketingDataTrendDtoList;
	}

	@Override
	public List<MarketingDataTrendDTO> queryPlatformOrderUserCountList(ActivityCouponCountQuery query) {
		QueryMap map = new QueryMap();
		map.put("couponId", query.getCouponId());
		map.put("source", query.getSource());
		map.put("endTime", query.getEndTime());
		SQLOperation operation = this.getSQLAndParams("CouponCountStatement.queryPlatformOrderUserCountList", map);
		List<MarketingDataTrendDTO> dataTrendDTOList = query(operation.getSql(), MarketingDataTrendDTO.class, operation.getParams());
		if (!CollectionUtil.isEmpty(dataTrendDTOList)) {
			return dataTrendDTOList;
		}
		ArrayList<MarketingDataTrendDTO> dataTrendDtoArrayList = new ArrayList<>();
		return dataTrendDtoArrayList;
	}

	@Override
	public MarketingUserStatisticsDTO getPlatformVisitCount(ActivityCouponCountQuery query) {
		QueryMap map = new QueryMap();
		map.put("couponId", query.getCouponId());
		map.put("source", query.getSource());
		map.put("beginTime", query.getBeginTime());
		map.put("endTime", query.getEndTime());
		SQLOperation operation = this.getSQLAndParams("CouponCountStatement.getPlatformVisitCount", map);
		MarketingUserStatisticsDTO marketingUserStatisticsDTO = get(operation.getSql(), MarketingUserStatisticsDTO.class, operation.getParams());
		if (!ObjectUtil.isEmpty(marketingUserStatisticsDTO)) {
			return marketingUserStatisticsDTO;
		}
		return new MarketingUserStatisticsDTO();
	}

	@Override
	public MarketingUserStatisticsDTO getPlatformCouponsReceivedCount(ActivityCouponCountQuery query) {
		QueryMap map = new QueryMap();
		map.put("couponId", query.getCouponId());
		map.put("source", query.getSource());
		map.put("beginTime", query.getBeginTime());
		map.put("endTime", query.getEndTime());
		SQLOperation operation = this.getSQLAndParams("CouponCountStatement.getPlatformCouponsReceivedCount", map);
		MarketingUserStatisticsDTO marketingUserStatisticsDTO = get(operation.getSql(), MarketingUserStatisticsDTO.class, operation.getParams());
		if (!ObjectUtil.isEmpty(marketingUserStatisticsDTO)) {
			return marketingUserStatisticsDTO;
		}
		return new MarketingUserStatisticsDTO();
	}
}
