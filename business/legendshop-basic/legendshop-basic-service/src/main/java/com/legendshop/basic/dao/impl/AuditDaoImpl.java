/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.basic.dao.AuditDao;
import com.legendshop.basic.dto.AuditDTO;
import com.legendshop.basic.entity.Audit;
import com.legendshop.basic.enums.AuditTypeEnum;
import com.legendshop.basic.query.AuditQuery;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * 店铺审核Dao
 *
 * @author legendshop
 */
@Repository
public class AuditDaoImpl extends GenericDaoImpl<Audit, Long> implements AuditDao {

	@Override
	public List<Audit> getShopAuditInfo(Long shopId) {
		return this.queryByProperties(new EntityCriterion().eq("shopId", shopId).addDescOrder("createTime"));
	}

	@Override
	public PageSupport<Audit> page(AuditQuery auditQuery) {
		CriteriaQuery cq = new CriteriaQuery(Audit.class, auditQuery.getPageSize(), auditQuery.getCurPage());
		cq.eq("commonId", auditQuery.getCommonId());
		cq.eq("auditType", auditQuery.getAuditType());
		cq.addDescOrder("auditTime");
		return queryPage(cq);
	}

	@Override
	public String getAuditOptionByShopId(Long shopId) {
		Integer auditType = AuditTypeEnum.SHOP_DETAIL.getValue();
		return this.get("select audit_opinion from ls_audit where common_id = ? and audit_type = ? order by audit_time desc Limit 1", String.class, shopId, auditType);
	}

	@Override
	public AuditDTO getByAuditTypeAndCommonId(Long commonId, AuditTypeEnum auditTypeEnum) {
		return get("select * from ls_audit where common_id=? and audit_type=? ORDER BY audit_time desc  LIMIT 0,1", AuditDTO.class, commonId, auditTypeEnum.getValue());
	}

	@Override
	public List<AuditDTO> getAuditDtoListByAuditTypeAndCommonId(Long commonId, Integer auditType) {
		return query("select * from ls_audit where common_id=? and audit_type=? ORDER BY audit_time desc ", AuditDTO.class, commonId, auditType);
	}

	@Override
	public List<AuditDTO> getAuditDtoListByCommonId(List<Long> commonId, Integer auditType) {
		LambdaEntityCriterion<AuditDTO> criterion = new LambdaEntityCriterion<>(AuditDTO.class);
		criterion.in(AuditDTO::getCommonId, commonId);
		criterion.eq(AuditDTO::getAuditType, auditType);
		return queryDTOByProperties(criterion);
	}

	@Override
	public Integer getAuditedShopCount() {
		return get(getSQL("Audit.getAuditedShopCount"), Integer.class);
	}

	@Override
	public Integer getAuditedBrandCount() {
		return get(getSQL("Audit.getAuditedBrandCount"), Integer.class);
	}

	@Override
	public Integer getAuditedProductCount() {
		return get(getSQL("Audit.getAuditedProductCount"), Integer.class);
	}

	@Override
	public Integer getAuditedReportProductCount() {
		return get(getSQL("Audit.getAuditedReportProductCount"), Integer.class);
	}

	@Override
	public Integer getAuditedProductCommentCount() {
		return get(getSQL("Audit.getAuditedProductCommentCount"), Integer.class);
	}

	@Override
	public Integer getAuditedAfterSaleCount() {
		return get(getSQL("Audit.getAuditedAfterSaleCount"), Integer.class);
	}

	@Override
	public Integer getAuditedBillingCount() {
		return get(getSQL("Audit.getAuditedBillingCount"), Integer.class);
	}

	@Override
	public Integer getAuditedBalanceWithdrawCount() {
		return get(getSQL("Audit.getAuditedBalanceWithdrawCount"), Integer.class);
	}

	@Override
	public Integer getAuditedFeedbackCount() {
		return get(getSQL("Audit.getAuditedFeedbackCount"), Integer.class);
	}

	@Override
	public List<AuditDTO> queryNewByAuditTypeAndCommonId(List<Long> commonId, Integer auditType) {
		if (CollUtil.isEmpty(commonId) || ObjectUtil.isEmpty(auditType)) {
			return Collections.emptyList();
		}

		QueryMap map = new QueryMap();
		map.put("auditType", auditType);
		map.in("commonId", commonId);

		SQLOperation operation = getSQLAndParams("Audit.queryNewByAuditTypeAndCommonId", map);
		return query(operation.getSql(), AuditDTO.class, operation.getParams());
	}


}
