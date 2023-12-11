/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.common.datasource.NonTable;
import com.legendshop.data.dto.DataUserAmountDTO;
import com.legendshop.data.dto.UserDataViewsDTO;
import com.legendshop.user.dao.UserCountDao;
import com.legendshop.user.dto.*;
import com.legendshop.user.query.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author legendshop
 */
@Repository
public class UserCountDaoImpl extends GenericDaoImpl<NonTable, Long> implements UserCountDao {

	@Override
	public Integer getNewUserData(String source, Date startDate, Date endDate) {

		QueryMap map = new QueryMap();
		map.put("source", source);
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		SQLOperation operation = this.getSQLAndParams("UserCount.queryNewUser", map);
		return get(operation.getSql(), operation.getParams(), new SingleColumnRowMapper<>(Integer.class));

	}

	@Override
	public Integer getTotalUserData(String source, Date startDate, Date endDate) {

		QueryMap map = new QueryMap();
		map.put("source", source);
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		SQLOperation operation = this.getSQLAndParams("UserCount.queryAllUser", map);
		return get(operation.getSql(), operation.getParams(), new SingleColumnRowMapper<>(Integer.class));
	}

	@Override
	public UserDataViewsDTO getUserViewData() {
		return null;
	}

	@Override
	public Integer getConsumerData(String source, Date startDate, Date endDate) {

		QueryMap map = new QueryMap();
		map.put("source", source);
		map.put("startDate", startDate);
		map.put("endDate", endDate);

		SQLOperation operation = this.getSQLAndParams("UserCount.queryConsumptionUser", map);
		return get(operation.getSql(), operation.getParams(), new SingleColumnRowMapper<>(Integer.class));
	}

	@Override
	public List<UserDataAreaDTO> queryDistributedArea(Date endDate) {

		return query(getSQL("UserCount.queryDistributedAreaUser"), UserDataAreaDTO.class, endDate);

	}

	@Override
	public PageSupport<UserDataAreaDTO> queryDistributedAreaPage(UserDataAreaQuery userDataAreaQuery) {
		QueryMap queryMap = new QueryMap();
		queryMap.put("endDate", userDataAreaQuery.getEndDate());
		queryMap.put("startDate", userDataAreaQuery.getStartDate());
		SimpleSqlQuery sqlQuery = new SimpleSqlQuery(UserDataAreaDTO.class, userDataAreaQuery);
		sqlQuery.setSqlAndParameter("UserCount.queryDistributedAreaUserPage", queryMap);
		return querySimplePage(sqlQuery);
	}

	@Override
	public List<UserDataAreaDTO> getDistributedAreaExcel(UserDataAreaQuery userDataAreaQuery) {
		QueryMap queryMap = new QueryMap();
		queryMap.put("endDate", userDataAreaQuery.getEndDate());
		queryMap.put("startDate", userDataAreaQuery.getStartDate());
		SQLOperation operation = this.getSQLAndParams("UserCount.queryDistributedAreaUserPage", queryMap);
		return this.query(operation.getSql(), UserDataAreaDTO.class, operation.getParams());
	}

	@Override
	public List<UserDataGradeDTO> queryDistributedGrade(Date endDate) {
		return query(getSQL("UserCount.queryDistributedGradeUser"), UserDataGradeDTO.class, endDate);
	}

	@Override
	public Integer queryUserCount(Date endDate) {
		return get(getSQL("UserCount.queryUserCount"), Integer.class, endDate);
	}

	@Override
	public List<UserDataPurchasingPowerDTO> queryPurchasingPower(Date endDate) {
		return query(getSQL("UserCount.queryPurchasingPower"), UserDataPurchasingPowerDTO.class, endDate);
	}

	@Override
	public List<UserDataShopSaleDTO> queryShopSaleData(Date endDate) {
		return query(getSQL("UserCount.queryShopSale"), UserDataShopSaleDTO.class, endDate);
	}

	@Override
	public DataUserAmountDTO getUserAmountLine(UserCountAmountQuery amountQuery) {
		QueryMap map = new QueryMap();
		map.put("startDate", amountQuery.getStartDate());
		map.put("endDate", amountQuery.getEndDate());

		SQLOperation operation = this.getSQLAndParams("UserCount.queryUserAmountLine", map);
		return get(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(DataUserAmountDTO.class));
	}

	@Override
	public PageSupport<DataUserAmountDTO> queryUserAmountPage(UserCountAmountQuery amountQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(DataUserAmountDTO.class, amountQuery.getPageSize(), amountQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("startDate", amountQuery.getStartDate());
		map.put("endDate", amountQuery.getEndDate());
		if (ObjectUtil.isNotEmpty(amountQuery.getProp()) && ObjectUtil.isNotEmpty(amountQuery.getOrder())) {
			map.put("orderBy", " order by " + amountQuery.getProp() + " " + amountQuery.getOrder());
		}

		query.setSqlAndParameter("UserCount.queryUserAmountData", map);
		PageSupport<DataUserAmountDTO> page = querySimplePage(query);
		return page;
	}

	@Override
	public List<DataUserAmountDTO> queryUserAmountPageExcel(UserCountAmountQuery amountQuery) {
		QueryMap map = new QueryMap();
		map.put("startDate", amountQuery.getStartDate());
		map.put("endDate", amountQuery.getEndDate());
		if (ObjectUtil.isNotEmpty(amountQuery.getProp()) && ObjectUtil.isNotEmpty(amountQuery.getOrder())) {
			map.put("orderBy", " order by " + amountQuery.getProp() + " " + amountQuery.getOrder());
		}

		SQLOperation operation = this.getSQLAndParams("UserCount.queryUserAmountDataExcel", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(DataUserAmountDTO.class));
	}

	@Override
	public List<UserDataPurchasingPageDTO> queryPurchasingPic(UserPurchasingQuery purchasingQuery) {
		QueryMap map = new QueryMap();
		map.put("userId", purchasingQuery.getUserId());
		map.put("mobile", purchasingQuery.getMobile());
		map.put("startDate", purchasingQuery.getStartDate());
		map.put("endDate", purchasingQuery.getEndDate());
		if (ObjectUtil.isNotEmpty(purchasingQuery.getProp()) && ObjectUtil.isNotEmpty(purchasingQuery.getOrder())) {
			map.put("orderBy", " order by " + purchasingQuery.getProp() + " " + purchasingQuery.getOrder());
		}
		SQLOperation operation = this.getSQLAndParams("UserCount.queryPurchasingPic", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(UserDataPurchasingPageDTO.class));
	}

	@Override
	public PageSupport<UserDataPurchasingPageDTO> queryPurchasingPage(UserPurchasingQuery purchasingQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(UserDataPurchasingPageDTO.class, purchasingQuery.getPageSize(), purchasingQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("userId", purchasingQuery.getUserId());
		map.put("mobile", purchasingQuery.getMobile());
		map.put("startDate", purchasingQuery.getStartDate());
		map.put("endDate", purchasingQuery.getEndDate());
		if (ObjectUtil.isNotEmpty(purchasingQuery.getProp()) && ObjectUtil.isNotEmpty(purchasingQuery.getOrder())) {
			map.put("orderBy", " order by " + purchasingQuery.getProp() + " " + purchasingQuery.getOrder());
		}
		query.setSqlAndParameter("UserCount.queryPurchasingPage", map);
		PageSupport<UserDataPurchasingPageDTO> page = querySimplePage(query);
		return page;
	}

	@Override
	public List<UserDataPurchasingPageDTO> queryPurchasingPageExcel(UserPurchasingQuery purchasingQuery) {
		QueryMap map = new QueryMap();
		map.put("userId", purchasingQuery.getUserId());
		map.put("mobile", purchasingQuery.getMobile());
		map.put("startDate", purchasingQuery.getStartDate());
		map.put("endDate", purchasingQuery.getEndDate());
		if (ObjectUtil.isNotEmpty(purchasingQuery.getProp()) && ObjectUtil.isNotEmpty(purchasingQuery.getOrder())) {
			map.put("orderBy", " order by " + purchasingQuery.getProp() + " " + purchasingQuery.getOrder());
		}
		SQLOperation operation = this.getSQLAndParams("UserCount.queryPurchasingPageExcel", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(UserDataPurchasingPageDTO.class));
	}

	@Override
	public PageSupport<LoginHistoryDTO> queryLoginHistoryPage(UserCountLoginQuery loginQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(LoginHistoryDTO.class, loginQuery.getPageSize(), loginQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.like("userId", loginQuery.getUserId(), MatchMode.ANYWHERE);
		map.like("nickName", loginQuery.getNickName(), MatchMode.ANYWHERE);
		map.put("startDate", loginQuery.getStartDate());
		map.put("endDate", loginQuery.getEndDate());

		query.setSqlAndParameter("UserCount.queryLoginHistory", map);
		PageSupport<LoginHistoryDTO> page = querySimplePage(query);
		return page;
	}

	@Override
	public PageSupport<LoginHistoryCountDTO> queryLoginHistoryCountPage(UserCountLoginQuery loginQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(LoginHistoryCountDTO.class, loginQuery.getPageSize(), loginQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.like("userId", loginQuery.getUserId(), MatchMode.ANYWHERE);
		map.put("startDate", loginQuery.getStartDate());
		map.put("endDate", loginQuery.getEndDate());
		map.like("nickName", loginQuery.getNickName());

		query.setSqlAndParameter("UserCount.queryLoginHistoryData", map);
		PageSupport<LoginHistoryCountDTO> page = querySimplePage(query);
		return page;
	}

	@Override
	public PageSupport<UserDataSmsDTO> querySmsHistory(UserCountSmsQuery smsQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(UserDataSmsDTO.class, smsQuery.getPageSize(), smsQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.like("userId", smsQuery.getUserId(), MatchMode.ANYWHERE);
		map.like("nickName", smsQuery.getNickName(), MatchMode.ANYWHERE);
		map.like("mobile", smsQuery.getMobile(), MatchMode.ANYWHERE);
		map.put("success", smsQuery.getSuccess());
		map.put("fail", smsQuery.getFail());
		map.put("startDate", smsQuery.getStartDate());
		map.put("endDate", smsQuery.getEndDate());

		query.setSqlAndParameter("UserCount.querySmsHistory", map);
		PageSupport<UserDataSmsDTO> page = querySimplePage(query);
		return page;
	}

	@Override
	public PageSupport<UserDataShopSalePageDTO> getShopSalePage(UserCountShopSaleQuery saleQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(UserDataShopSalePageDTO.class, saleQuery.getPageSize(), saleQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.like("shopName", saleQuery.getShopName(), MatchMode.ANYWHERE);
		map.put("startDate", saleQuery.getStartDate());
		map.put("endDate", saleQuery.getEndDate());

		if (ObjectUtil.isNotEmpty(saleQuery.getProp()) && ObjectUtil.isNotEmpty(saleQuery.getOrder())) {
			map.put("orderBy", " order by " + saleQuery.getProp() + " " + saleQuery.getOrder());
		}

		query.setSqlAndParameter("UserCount.getShopSalePage", map);
		PageSupport<UserDataShopSalePageDTO> page = querySimplePage(query);
		return page;
	}

	@Override
	public List<UserDataShopSalePageDTO> getShopSalePic(UserCountShopSaleQuery saleQuery) {
		QueryMap map = new QueryMap();
		map.like("shopName", saleQuery.getShopName(), MatchMode.ANYWHERE);
		map.put("startDate", saleQuery.getStartDate());
		map.put("endDate", saleQuery.getEndDate());

		SQLOperation operation = this.getSQLAndParams("UserCount.getShopSalePic", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(UserDataShopSalePageDTO.class));
	}

	@Override
	public List<UserDataShopSalePageDTO> getShopSalePageExcel(UserCountShopSaleQuery saleQuery) {
		QueryMap map = new QueryMap();
		map.like("shopName", saleQuery.getShopName(), MatchMode.ANYWHERE);
		map.put("startDate", saleQuery.getStartDate());
		map.put("endDate", saleQuery.getEndDate());

		if (ObjectUtil.isNotEmpty(saleQuery.getProp()) && ObjectUtil.isNotEmpty(saleQuery.getOrder())) {
			map.put("orderBy", " order by " + saleQuery.getProp() + " " + saleQuery.getOrder());
		}
		SQLOperation operation = this.getSQLAndParams("UserCount.getShopSalePageExcel", map);
		return query(operation.getSql(), operation.getParams(), new BeanPropertyRowMapper<>(UserDataShopSalePageDTO.class));
	}

	@Override
	public String queryNickNameById(Long userId) {
		return get(getSQL("UserCount.queryNickNameById"), String.class, userId);
	}

}
