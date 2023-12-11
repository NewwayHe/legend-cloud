/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.bo.PendingMattersAdminBO;
import com.legendshop.basic.dto.AuditDTO;
import com.legendshop.basic.enums.AuditTypeEnum;
import com.legendshop.basic.query.AuditQuery;
import com.legendshop.common.core.constant.R;

import java.util.List;

/**
 * @author legendshop
 */
public interface AuditService {

	/**
	 * 审核动作
	 *
	 * @param auditDTO
	 * @return
	 */
	R audit(AuditDTO auditDTO);

	R audit(List<AuditDTO> auditDTOList);

	String getAuditOptionByShopId(Long shopId);

	/**
	 * 查询审核意见
	 *
	 * @param commonId      通用id（审核类型为商品，这个id就为商品id，其他类型类似）
	 * @param auditTypeEnum 审核类型 {@link AuditTypeEnum}
	 * @return
	 */
	AuditDTO getByAuditTypeAndCommonId(Long commonId, AuditTypeEnum auditTypeEnum);

	Long save(AuditDTO auditDTO);

	PageSupport<AuditDTO> page(AuditQuery auditQuery);

	/**
	 * 批量保存
	 *
	 * @param auditDTO
	 * @return
	 */
	R batchSave(AuditDTO auditDTO);

	List<AuditDTO> getAuditDtoListByAuditTypeAndCommonId(Long commonId, Integer auditType);

	List<AuditDTO> checkWalletDetails(List<Long> commonId);

	PendingMattersAdminBO getPending();

	/**
	 * 批量查询最新的审核记录
	 *
	 * @param commonId
	 * @param auditTypeEnum
	 * @return
	 */
	List<AuditDTO> queryNewByAuditTypeAndCommonId(List<Long> commonId, AuditTypeEnum auditTypeEnum);
}
