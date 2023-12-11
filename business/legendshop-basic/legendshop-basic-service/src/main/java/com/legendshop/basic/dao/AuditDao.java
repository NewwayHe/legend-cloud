/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.AuditDTO;
import com.legendshop.basic.entity.Audit;
import com.legendshop.basic.enums.AuditTypeEnum;
import com.legendshop.basic.query.AuditQuery;

import java.util.List;

/**
 * 店铺审核Dao
 *
 * @author legendshop
 */
public interface AuditDao extends GenericDao<Audit, Long> {

	List<Audit> getShopAuditInfo(Long shopId);

	PageSupport<Audit> page(AuditQuery auditQuery);

	String getAuditOptionByShopId(Long shopId);

	/**
	 * 查询审核意见
	 *
	 * @param commonId      通用id（审核类型为商品，这个id就为商品id，其他类型类似）
	 * @param auditTypeEnum 审核类型 {@link AuditTypeEnum}
	 * @return
	 */
	AuditDTO getByAuditTypeAndCommonId(Long commonId, AuditTypeEnum auditTypeEnum);

	/**
	 * 获取所有的审核详细
	 *
	 * @param commonId
	 * @return
	 */
	List<AuditDTO> getAuditDtoListByAuditTypeAndCommonId(Long commonId, Integer auditType);

	List<AuditDTO> getAuditDtoListByCommonId(List<Long> commonId, Integer auditType);

	/**
	 * 获得所有未审核店铺数量
	 *
	 * @return
	 */
	Integer getAuditedShopCount();

	/**
	 * 获得所有未审核品牌数量
	 *
	 * @return
	 */
	Integer getAuditedBrandCount();

	/**
	 * 获得所有待审核商品数量
	 *
	 * @return
	 */
	Integer getAuditedProductCount();

	/**
	 * 获得所有待审核举报商品数量
	 *
	 * @return
	 */
	Integer getAuditedReportProductCount();

	/**
	 * 获得所有待审核商品评论数量
	 *
	 * @return
	 */
	Integer getAuditedProductCommentCount();

	/**
	 * 获得所有待审核售后数量
	 *
	 * @return
	 */
	Integer getAuditedAfterSaleCount();

	/**
	 * 获得所有待处理账单结算数量
	 *
	 * @return
	 */
	Integer getAuditedBillingCount();

	/**
	 * 获得所有待处理余额提现数量
	 *
	 * @return
	 */
	Integer getAuditedBalanceWithdrawCount();

	/**
	 * 获得所有待处理意见反馈数量
	 *
	 * @return
	 */
	Integer getAuditedFeedbackCount();

	/**
	 * @param commonId
	 * @param auditType
	 * @return
	 */
	List<AuditDTO> queryNewByAuditTypeAndCommonId(List<Long> commonId, Integer auditType);
}
