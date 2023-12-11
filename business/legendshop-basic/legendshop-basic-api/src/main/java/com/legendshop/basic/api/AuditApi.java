/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.AuditDTO;
import com.legendshop.basic.enums.AuditTypeEnum;
import com.legendshop.basic.query.AuditQuery;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 审核服务
 *
 * @author legendshop
 */
@FeignClient(contextId = "AuditApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface AuditApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	/**
	 * 审核动作
	 *
	 * @param auditDTO
	 * @return
	 */
	@PostMapping(value = PREFIX + "/audit")
	R audit(@RequestBody AuditDTO auditDTO);

	/**
	 * 批量审核动作
	 *
	 * @param auditDTOList
	 * @return
	 */
	@PostMapping(value = PREFIX + "/audit/batch")
	R audit(@RequestBody List<AuditDTO> auditDTOList);

	@GetMapping(value = PREFIX + "/audit/getAuditOptionByShopId")
	R<String> getAuditOptionByShopId(@RequestParam(value = "shopId") Long shopId);

	/**
	 * 查询审核意见
	 *
	 * @param commonId      通用id（审核类型为商品，这个id就为商品id，其他类型类似）
	 * @param auditTypeEnum 审核类型 {@link AuditTypeEnum}
	 * @return
	 */
	@GetMapping(value = PREFIX + "/audit/getByAuditTypeAndCommonId")
	R<AuditDTO> getByAuditTypeAndCommonId(@RequestParam(value = "commonId") Long commonId, @RequestParam(value = "auditTypeEnum") AuditTypeEnum auditTypeEnum);

	/**
	 * 查询审核意见
	 *
	 * @param commonId      通用id（审核类型为商品，这个id就为商品id，其他类型类似）
	 * @param auditTypeEnum 审核类型 {@link AuditTypeEnum}
	 * @return
	 */
	@GetMapping(value = PREFIX + "/audit/queryNewByAuditTypeAndCommonId")
	R<List<AuditDTO>> queryNewByAuditTypeAndCommonId(@RequestBody List<Long> commonId, @RequestParam(value = "auditTypeEnum") AuditTypeEnum auditTypeEnum);

	/**
	 * 保存审核信息
	 *
	 * @param auditDTO
	 * @return
	 */
	@PostMapping(value = PREFIX + "/audit/save")
	R<Long> save(@RequestBody AuditDTO auditDTO);


	/**
	 * 批量保存审核信息
	 *
	 * @param auditDTO
	 * @return
	 */
	@PostMapping(value = PREFIX + "/audit/batchSave")
	R batchSave(@RequestBody AuditDTO auditDTO);

	/**
	 * 查询审核记录
	 *
	 * @param auditQuery
	 * @return
	 */
	@PostMapping(value = PREFIX + "/audit/page")
	R<PageSupport<AuditDTO>> page(@RequestBody AuditQuery auditQuery);

	/**
	 * 提现记录是否已审核
	 */
	@PostMapping(value = PREFIX + "/audit/wallet/isAudit")
	R<List<AuditDTO>> checkWalletDetails(@RequestBody List<Long> commonId);


}
