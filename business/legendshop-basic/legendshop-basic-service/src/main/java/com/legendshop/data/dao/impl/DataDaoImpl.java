/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dao.impl;

import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.QueryMap;
import com.legendshop.common.datasource.NonTable;
import com.legendshop.data.dao.DataDao;
import com.legendshop.data.dto.*;
import com.legendshop.data.entity.DataUserPurchasing;
import com.legendshop.data.query.ActivityDataQuery;
import com.legendshop.data.query.SimpleQuery;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author legendshop
 */
@Repository
public class DataDaoImpl extends GenericDaoImpl<NonTable, Long> implements DataDao {


	@Override
	public DataUserAmountDTO getLastedDataDate() {
		return get(getSQL("DataCount.queryLastedDataDate"), DataUserAmountDTO.class);
	}

	@Override
	public DataUserAmountDTO getUserAmountNewData(Date startDate, Date endDate) {

		QueryMap map = new QueryMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		SQLOperation operation = this.getSQLAndParams("DataCount.queryUserNew", map);
		return get(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(DataUserAmountDTO.class));

	}

	@Override
	public Integer getUserCount(Date endDate) {
		return get(getSQL("DataCount.queryUserCount"), Integer.class, endDate);
	}

	@Override
	public Integer getUserDetailNum() {
		return get(getSQL("DataCount.queryUserExist"), Integer.class);
	}

	@Override
	public Integer queryUserPurchasingExist(Long userId) {
		return get(getSQL("DataCount.queryPurchasingUserExist"), Integer.class, userId);
	}

	@Override
	public DataUserPurchasing queryPurchasingDataById(Long userId) {

		return get(getSQL("DataCount.queryPurchasingDataById"), DataUserPurchasing.class, userId);
	}

	@Override
	public NameAndMobileDTO queryMobileById(Long userId) {
		return get(getSQL("DataCount.queryMobileById"), NameAndMobileDTO.class, userId);
	}

	@Override
	public ProductViewDTO queryViewDataById(Long prodId, Date time, String source) {

		if (source == null) {
			return null;
		}

		QueryMap map = new QueryMap();
		map.put("id", prodId);
		map.put("time", time);
		map.put("source", source);

		SQLOperation operation = this.getSQLAndParams("DataCount.queryProdViewDataById", map);
		return get(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductViewDTO.class));
	}

	@Override
	public ProductViewDTO queryProdViewNum(String source, Date startDate, Date endDate, Long shopId) {

		QueryMap map = new QueryMap();
		map.put("source", source);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("shopId", shopId);

		SQLOperation operation = this.getSQLAndParams("DataCount.queryProdViewNum", map);
		return get(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ProductViewDTO.class));
	}

	@Override
	public Integer queryCartNum(Date startDate, Date endDate, Long shopId) {
		QueryMap map = new QueryMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("shopId", shopId);

		SQLOperation operation = this.getSQLAndParams("DataCount.queryCartNum", map);
		return get(operation.getSql(), operation.getParams(), new SingleColumnRowMapper<>(Integer.class));
	}

	@Override
	public Integer queryFavoriteNum(SimpleQuery simpleQuery) {
		QueryMap map = new QueryMap();
		map.put("startDate", simpleQuery.getStartDate());
		map.put("endDate", simpleQuery.getEndDate());
		map.put("shopId", simpleQuery.getShopId());
		map.put("source", simpleQuery.getSource());

		SQLOperation operation = this.getSQLAndParams("DataCount.queryFavoriteNum", map);
		return get(operation.getSql(), operation.getParams(), new SingleColumnRowMapper<>(Integer.class));
	}

	@Override
	public BusinessDataDTO queryBusinessData(SimpleQuery simpleQuery) {
		QueryMap map = new QueryMap();
		map.put("source", simpleQuery.getSource());
		map.put("startDate", simpleQuery.getStartDate());
		map.put("endDate", simpleQuery.getEndDate());
		map.put("shopId", simpleQuery.getShopId());

		SQLOperation operation = this.getSQLAndParams("DataCount.queryBusinessData", map);
		return get(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(BusinessDataDTO.class));
	}

	@Override
	public ReturnOrderDataDTO queryReturnOrder(Date startDate, Date endDate, Long shopId) {
		QueryMap map = new QueryMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("shopId", shopId);

		SQLOperation operation = this.getSQLAndParams("DataCount.queryReturnOrder", map);
		return get(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ReturnOrderDataDTO.class));
	}

	@Override
	public DealOrderPicDTO queryDealOrderPic(Date startDate, Date endDate, String source, Long shopId) {
		QueryMap map = new QueryMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("shopId", shopId);

		SQLOperation operation = this.getSQLAndParams("DataCount.queryDealOrderPic", map);
		return get(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(DealOrderPicDTO.class));
	}

	@Override
	public Integer queryReturnOrderPic(Date startDate, Date endDate, Integer applyType, Long shopId) {
		QueryMap map = new QueryMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("applyType", applyType);
		map.put("shopId", shopId);

		SQLOperation operation = this.getSQLAndParams("DataCount.queryReturnOrderPic", map);
		return get(operation.getSql(), operation.getParams(), new SingleColumnRowMapper<>(Integer.class));
	}

	@Override
	public Integer queryPayGoodNum(Date startDate, Date endDate, Long shopId) {
		QueryMap map = new QueryMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("shopId", shopId);

		SQLOperation operation = this.getSQLAndParams("DataCount.queryPayGoodNum", map);
		return get(operation.getSql(), operation.getParams(), new SingleColumnRowMapper<>(Integer.class));
	}

	@Override
	public ShopViewDTO queryShopViewDataById(Long shopId, Date time, String source) {
		QueryMap map = new QueryMap();
		map.put("id", shopId);
		map.put("time", time);
		map.put("source", source);

		SQLOperation operation = this.getSQLAndParams("DataCount.queryShopViewDataById", map);
		return get(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ShopViewDTO.class));
	}

	@Override
	public ShopViewDTO getShopViewPic(Long shopId, Date startDate, Date endDate, String source) {
		QueryMap map = new QueryMap();
		map.put("shopId", shopId);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("source", source);

		SQLOperation operation = this.getSQLAndParams("DataCount.getShopViewPic", map);
		return get(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ShopViewDTO.class));
	}

	@Override
	public DataActivityCollectDTO getLastedActivityData() {
		return get(getSQL("DataCount.getLastedActivityData"), DataActivityCollectDTO.class);
	}

	@Override
	public Boolean queryIfNewCustomer(Long userId, Date time) {
		QueryMap map = new QueryMap();
		map.put("userId", userId);
		map.put("time", time);

		SQLOperation operation = this.getSQLAndParams("DataCount.queryIfNewCustomer", map);

		Long id = get(operation.getSql(), operation.getParams(), new SingleColumnRowMapper<>(Long.class));
		return id == null;
	}

	@Override
	public List<ActivityCollectDTO> couponCollect(Date startDate, Date endDate, List<Long> activityIds) {
		QueryMap map = new QueryMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.in("activityIds", activityIds);

		SQLOperation operation = this.getSQLAndParams("DataCount.couponCollect", map);

		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ActivityCollectDTO.class));
	}

	@Override
	public List<ActivityCollectDTO> marketingLimitCollect(Date startDate, Date endDate, List<Long> activityIds) {
		QueryMap map = new QueryMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.in("activityIds", activityIds);

		SQLOperation operation = this.getSQLAndParams("DataCount.marketingLimitCollect", map);

		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ActivityCollectDTO.class));
	}

	@Override
	public List<ActivityCollectDTO> marketingRewardCollect(Date startDate, Date endDate, List<Long> activityIds) {
		QueryMap map = new QueryMap();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.in("activityIds", activityIds);

		SQLOperation operation = this.getSQLAndParams("DataCount.marketingRewardCollect", map);

		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ActivityCollectDTO.class));
	}

	@Override
	public List<DataActivityCollectDTO> getActivityPublishPic(ActivityDataQuery activityDataQuery) {
		QueryMap map = new QueryMap();
		map.put("startDate", activityDataQuery.getStartDate());
		map.put("endDate", activityDataQuery.getEndDate());
		map.put("shopId", activityDataQuery.getShopId());

		SQLOperation operation = this.getSQLAndParams("DataCount.getActivityPublishPic", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(DataActivityCollectDTO.class));
	}

	@Override
	public List<DataActivityOrderDTO> getActivityDealPic(ActivityDataQuery activityDataQuery) {
		QueryMap map = new QueryMap();
		map.put("startDate", activityDataQuery.getStartDate());
		map.put("endDate", activityDataQuery.getEndDate());
		map.put("idType", activityDataQuery.getIdType());

		SQLOperation operation = this.getSQLAndParams("DataCount.getActivityDealPic", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(DataActivityOrderDTO.class));
	}

	@Override
	public List<ActivityPayDataDTO> getActivityPayData(ActivityDataQuery activityDataQuery) {
		QueryMap map = new QueryMap();
		map.put("startDate", activityDataQuery.getStartDate());
		map.put("endDate", activityDataQuery.getEndDate());
		map.put("shopId", activityDataQuery.getShopId());

		SQLOperation operation = this.getSQLAndParams("DataCount.getActivityPayData", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(ActivityPayDataDTO.class));
	}

	@Override
	public List<BigDecimal> getPayData(ActivityDataQuery activityDataQuery) {
		QueryMap map = new QueryMap();
		map.put("startDate", activityDataQuery.getStartDate());
		map.put("endDate", activityDataQuery.getEndDate());
		map.put("shopId", activityDataQuery.getShopId());

		SQLOperation operation = this.getSQLAndParams("DataCount.getPayData", map);
		return query(operation.getSql(), operation.getParams(), new SingleColumnRowMapper<>(BigDecimal.class));
	}

	@Override
	public List<DataActivityViewDTO> queryActivityView(ActivityDataQuery activityDataQuery) {

		QueryMap map = new QueryMap();
		map.put("type", activityDataQuery.getType());
		map.put("startDate", activityDataQuery.getStartDate());
		map.put("endDate", activityDataQuery.getEndDate());
		map.in("ids", activityDataQuery.getIds());

		SQLOperation operation = this.getSQLAndParams("DataCount.queryActivityView", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(DataActivityViewDTO.class));

	}


}
