/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.*;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.pay.dao.UserWalletDetailsDao;
import com.legendshop.pay.dto.UserWalletDetailsDTO;
import com.legendshop.pay.dto.WalletSignatureDTO;
import com.legendshop.pay.entity.UserWalletDetails;
import com.legendshop.pay.enums.UserWalletAmountTypeEnum;
import com.legendshop.pay.enums.WalletBusinessTypeEnum;
import com.legendshop.pay.enums.WalletDetailsAuditEnum;
import com.legendshop.pay.enums.WalletDetailsStateEnum;
import com.legendshop.pay.excel.UserWalletDetailsExcelDTO;
import com.legendshop.pay.query.UserWalletDetailsQuery;
import com.legendshop.pay.service.convert.UserWalletDetailsConverter;
import com.legendshop.pay.utils.WalletUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 用户钱包收支记录详情(UserWalletDetails)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2021-03-13 14:44:00
 * <p>
 * 这类禁止大部分直接sql操作，需要维护签名正确
 */
@Repository
@RequiredArgsConstructor
public class UserWalletDetailsDaoImpl extends GenericDaoImpl<UserWalletDetails, Long> implements UserWalletDetailsDao {

	private final UserWalletDetailsConverter converter;

	@Override
	public Long save(UserWalletDetails entity) {
		if (null == entity) {
			return null;
		}
		entity.setSignature(WalletUtil.sign(JSONUtil.parseObj(new WalletSignatureDTO(this.converter.to(entity)))));
		return super.save(entity);
	}

	@Override
	public <S extends UserWalletDetails> List<Long> save(List<S> entities) {
		if (CollectionUtils.isEmpty(entities)) {
			return new ArrayList<>();
		}
		entities.forEach(e -> e.setSignature(WalletUtil.sign(JSONUtil.parseObj(new WalletSignatureDTO(this.converter.to(e))))));
		return super.save(entities);
	}

	@Override
	public <S extends UserWalletDetails> List<Long> saveWithId(List<S> entities) {
		if (CollectionUtils.isEmpty(entities)) {
			return new ArrayList<>();
		}
		entities.forEach(e -> e.setSignature(WalletUtil.sign(JSONUtil.parseObj(new WalletSignatureDTO(this.converter.to(e))))));
		return super.saveWithId(entities);
	}

	@Override
	public int update(UserWalletDetails entity) {
		if (null == entity) {
			return 0;
		}
		entity.setSignature(WalletUtil.sign(JSONUtil.parseObj(new WalletSignatureDTO(this.converter.to(entity)))));
		return super.update(entity);
	}

	@Override
	public int update(List<UserWalletDetails> entities) {
		if (CollectionUtils.isEmpty(entities)) {
			return 0;
		}
		entities.forEach(e -> e.setSignature(WalletUtil.sign(JSONUtil.parseObj(new WalletSignatureDTO(this.converter.to(e))))));
		return super.update(entities);
	}

	@Override
	public UserWalletDetails getBySerialNo(Long serialNo) {
		return super.get("SELECT * FROM ls_user_wallet_details WHERE serial_no = ? LIMIT 1", UserWalletDetails.class, serialNo);
	}

	@Override
	public List<UserWalletDetails> queryByUserId(Long userId) {
		return super.query("SELECT * FROM ls_user_wallet_details WHERE user_id = ? LIMIT 1", UserWalletDetails.class, userId);
	}

	@Override
	public List<UserWalletDetails> queryByUserId(Long userId, Date startDate, Date endDate) {
		LambdaEntityCriterion<UserWalletDetails> criterion = new LambdaEntityCriterion<>(UserWalletDetails.class);
		criterion.eq(UserWalletDetails::getUserId, userId)
				.ge(UserWalletDetails::getCreateTime, startDate)
				.le(UserWalletDetails::getCreateTime, endDate);
		return this.queryByProperties(criterion);
	}

	@Override
	public PageSupport<UserWalletDetailsDTO> associatePage(UserWalletDetailsQuery query) {
		SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery(UserWalletDetailsDTO.class, query.getPageSize(), query.getCurPage());
		QueryMap queryMap = new QueryMap();
		if (null != query.getGrade()) {
			queryMap.put("grade", query.getGrade());
		}

		if (WalletDetailsAuditEnum.WAIT.getCode().equals(query.getOpStatus())) {
			queryMap.put("opStatus", " and la.op_status is null");
		} else if (ObjectUtil.isNotEmpty(query.getOpStatus())) {
			queryMap.put("opStatus", " and la.op_status  =" + query.getOpStatus());
		}


		if (null != query.getUserId()) {
			queryMap.put("userId", query.getUserId());
		}

		if (null != query.getSerialNo()) {
			queryMap.put("serialNo", query.getSerialNo());
		}

		if (null != query.getBusinessId()) {
			queryMap.put("businessId", query.getBusinessId());
		}

		if (StringUtils.isNotBlank(query.getNickName())) {
			queryMap.like("nickName", query.getNickName(), MatchMode.ANYWHERE);
		}

		if (StringUtils.isNotBlank(query.getMobile())) {
			queryMap.like("mobile", query.getMobile(), MatchMode.ANYWHERE);
		}

		if (null != query.getBusinessType()) {
			queryMap.put("businessType", query.getBusinessType().name());
		}

		if (null != query.getOperationType()) {
			queryMap.put("operationType", query.getOperationType().name());
		}

		if (null != query.getState()) {
			queryMap.put("state", query.getState());
		}

		if (null != query.getStartDate() && null != query.getEndDate()) {
			queryMap.put("startDate", DateUtil.beginOfDay(query.getStartDate()));
			queryMap.put("endDate", DateUtil.endOfDay(query.getEndDate()));
		}

		if (null != query.getPayStartDate() && null != query.getPayEndDate()) {
			queryMap.put("payStartDate", DateUtil.beginOfDay(query.getPayStartDate()));
			queryMap.put("payEndDate", DateUtil.endOfDay(query.getPayEndDate()));
		}

		if (ObjectUtil.isNotEmpty(query.getStyle())) {
			queryMap.put("style", query.getStyle());
		}
		simpleSqlQuery.setSqlAndParameter("UserWalletDetails.associatePage", queryMap);
		return super.querySimplePage(simpleSqlQuery);
	}

	@Override
	public PageSupport<UserWalletDetails> pageList(UserWalletDetailsQuery query) {
		CriteriaQuery criteriaQuery = new CriteriaQuery(UserWalletDetails.class, query.getPageSize(), query.getCurPage());
		if (null != query.getUserId()) {
			criteriaQuery.eq("userId", query.getUserId());
		}

		if (null != query.getSerialNo()) {
			criteriaQuery.eq("serialNo", query.getSerialNo());
		}

		if (null != query.getBusinessId()) {
			criteriaQuery.eq("businessId", query.getBusinessId());
		}

		if (null != query.getBusinessType()) {
			criteriaQuery.eq("businessType", query.getBusinessType().name());
		}

		if (null != query.getOperationType()) {
			criteriaQuery.eq("operationType", query.getOperationType().name());
		}

		if (null != query.getState()) {
			criteriaQuery.eq("state", query.getState());
		}

		if (null != query.getStartDate() && null != query.getEndDate()) {
			criteriaQuery.ge("createTime", DateUtil.beginOfDay(query.getStartDate()));
			criteriaQuery.lt("createTime", DateUtil.endOfDay(query.getEndDate()));
		}
		criteriaQuery.eq("amountType", query.getAmountType());
		criteriaQuery.addDescOrder("createTime");
		return super.queryPage(criteriaQuery);
	}

	@Override
	public UserWalletDetails findUserPayDetails(Long businessId) {
		return super.get("SELECT * FROM ls_user_wallet_details WHERE business_id = ? AND business_type = ?", UserWalletDetails.class, businessId, WalletBusinessTypeEnum.PAYMENT_DEDUCTION.name());
	}

	@Override
	public List<UserWalletDetails> findOrderPayDetails(List<Long> businessIds) {
		return super.queryByProperties(new EntityCriterion().in("businessId", businessIds).eq("businessType", WalletBusinessTypeEnum.ORDER_DEDUCTION.name()));
	}

	@Override
	public List<UserWalletDetails> findDetailsByBusinessIds(List<Long> businessIds, WalletBusinessTypeEnum type) {
		return super.queryByProperties(new EntityCriterion().in("businessId", businessIds.toArray()).eq("businessType", type.name()));
	}

	@Override
	public List<UserWalletDetails> findDetailsByBusinessIds(List<Long> businessIds, WalletBusinessTypeEnum type, UserWalletAmountTypeEnum amountType) {
		return super.queryByProperties(new EntityCriterion().in("businessId", businessIds.toArray()).eq("businessType", type.name()).eq("amountType", amountType.name()));
	}

	@Override
	public List<UserWalletDetails> findDetailsByBusinessIds(List<Long> businessIds, WalletBusinessTypeEnum type, WalletDetailsStateEnum state) {
		EntityCriterion entityCriterion = new EntityCriterion();
		entityCriterion.in("businessId", businessIds);
		entityCriterion.eq("businessType", type.name());

		if (null != state) {
			entityCriterion.eq("state", state.getCode());
		}
		return super.queryByProperties(entityCriterion);
	}

	@Override
	public List<UserWalletDetails> getByIds(List<Long> ids) {
		if (CollUtil.isEmpty(ids)) {
			return Collections.emptyList();
		}
		LambdaEntityCriterion<UserWalletDetails> criterion = new LambdaEntityCriterion<>(UserWalletDetails.class);
		criterion.in(UserWalletDetails::getId, ids);
		return queryByProperties(criterion);
	}

	@Override
	public List<UserWalletDetailsExcelDTO> walletExcel(UserWalletDetailsQuery query) {

		QueryMap queryMap = new QueryMap();
		if (null != query.getGrade()) {
			queryMap.put("grade", query.getGrade());
		}

		if (WalletDetailsAuditEnum.WAIT.getCode().equals(query.getOpStatus())) {
			queryMap.put("opStatus", " and la.op_status is null");
		} else if (ObjectUtil.isNotEmpty(query.getOpStatus())) {
			queryMap.put("opStatus", " and la.op_status  =" + query.getOpStatus());
		}


		if (null != query.getUserId()) {
			queryMap.put("userId", query.getUserId());
		}

		if (null != query.getSerialNo()) {
			queryMap.put("serialNo", query.getSerialNo());
		}

		if (null != query.getBusinessId()) {
			queryMap.put("businessId", query.getBusinessId());
		}

		if (StringUtils.isNotBlank(query.getNickName())) {
			queryMap.like("nickName", query.getNickName(), MatchMode.ANYWHERE);
		}

		if (StringUtils.isNotBlank(query.getMobile())) {
			queryMap.like("mobile", query.getMobile(), MatchMode.ANYWHERE);
		}

		if (null != query.getBusinessType()) {
			queryMap.put("businessType", query.getBusinessType().name());
		}

		if (null != query.getOperationType()) {
			queryMap.put("operationType", query.getOperationType().name());
		}

		if (null != query.getState()) {
			queryMap.put("state", query.getState());
		}

		if (null != query.getStartDate() && null != query.getEndDate()) {
			queryMap.put("startDate", DateUtil.beginOfDay(query.getStartDate()));
			queryMap.put("endDate", DateUtil.endOfDay(query.getEndDate()));
		}

		SQLOperation sqlAndParams = getSQLAndParams("UserWalletDetails.associatePage", queryMap);
		return query(sqlAndParams.getSql(), UserWalletDetailsExcelDTO.class, sqlAndParams.getParams());
	}

	@Override
	public List<UserWalletDetails> findDetailsByBusinessId(Long businessId, WalletBusinessTypeEnum type) {
		LambdaEntityCriterion<UserWalletDetails> criterion = new LambdaEntityCriterion<>(UserWalletDetails.class);
		criterion.eq(UserWalletDetails::getBusinessId, businessId);
		criterion.eq(UserWalletDetails::getBusinessType, type.name());
		return queryByProperties(criterion);
	}
}
