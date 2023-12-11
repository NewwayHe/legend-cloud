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
import com.legendshop.basic.service.AuditService;
import com.legendshop.common.core.constant.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@RestController
@RequiredArgsConstructor
public class AuditApiImpl implements AuditApi {

	final AuditService auditService;

	@Override
	public R audit(@RequestBody AuditDTO auditDTO) {
		return this.auditService.audit(auditDTO);
	}

	@Override
	public R audit(@RequestBody List<AuditDTO> auditDTOList) {
		return this.auditService.audit(auditDTOList);
	}

	@Override
	public R<String> getAuditOptionByShopId(@RequestParam(value = "shopId") Long shopId) {
		return R.ok(this.auditService.getAuditOptionByShopId(shopId));
	}

	@Override
	public R<AuditDTO> getByAuditTypeAndCommonId(@RequestParam(value = "commonId") Long commonId, @RequestParam(value = "auditTypeEnum") AuditTypeEnum auditTypeEnum) {
		return R.ok(this.auditService.getByAuditTypeAndCommonId(commonId, auditTypeEnum));
	}

	@Override
	public R<List<AuditDTO>> queryNewByAuditTypeAndCommonId(@RequestBody List<Long> commonId, @RequestParam(value = "auditTypeEnum") AuditTypeEnum auditTypeEnum) {
		return R.ok(this.auditService.queryNewByAuditTypeAndCommonId(commonId, auditTypeEnum));
	}

	@Override
	public R<Long> save(@RequestBody AuditDTO auditDTO) {
		return R.ok(this.auditService.save(auditDTO));
	}

	@Override
	public R batchSave(@RequestBody AuditDTO auditDTO) {
		return R.ok(this.auditService.batchSave(auditDTO));
	}

	@Override
	public R<PageSupport<AuditDTO>> page(@RequestBody AuditQuery auditQuery) {
		return R.ok(this.auditService.page(auditQuery));
	}

	@Override
	public R<List<AuditDTO>> checkWalletDetails(@RequestBody List<Long> commonId) {
		return R.ok(auditService.checkWalletDetails(commonId));
	}
}
