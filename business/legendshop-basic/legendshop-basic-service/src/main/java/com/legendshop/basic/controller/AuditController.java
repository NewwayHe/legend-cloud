/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.AuditDTO;
import com.legendshop.basic.enums.AuditTypeEnum;
import com.legendshop.basic.query.AuditQuery;
import com.legendshop.basic.service.AuditService;
import com.legendshop.common.core.constant.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/audit")
public class AuditController {

	final AuditService auditService;

	@PostMapping
	public R audit(@RequestBody AuditDTO auditDTO) {
		return this.auditService.audit(auditDTO);
	}

	@PostMapping(value = "/batch")
	public R audit(@RequestBody List<AuditDTO> auditDTOList) {
		return this.auditService.audit(auditDTOList);
	}

	@GetMapping(value = "/getAuditOptionByShopId")
	public R<String> getAuditOptionByShopId(@RequestParam(value = "shopId") Long shopId) {
		return R.ok(this.auditService.getAuditOptionByShopId(shopId));
	}

	@GetMapping(value = "/getByAuditTypeAndCommonId")
	public R<AuditDTO> getByAuditTypeAndCommonId(@RequestParam(value = "commonId") Long commonId, @RequestParam(value = "auditTypeEnum") AuditTypeEnum auditTypeEnum) {
		return R.ok(this.auditService.getByAuditTypeAndCommonId(commonId, auditTypeEnum));
	}

	@GetMapping(value = "/queryNewByAuditTypeAndCommonId")
	public R<List<AuditDTO>> queryNewByAuditTypeAndCommonId(@RequestBody List<Long> commonId, @RequestParam(value = "auditTypeEnum") AuditTypeEnum auditTypeEnum) {
		return R.ok(this.auditService.queryNewByAuditTypeAndCommonId(commonId, auditTypeEnum));
	}

	@PostMapping(value = "/save")
	public R<Long> save(@RequestBody AuditDTO auditDTO) {
		return R.ok(this.auditService.save(auditDTO));
	}

	@PostMapping(value = "/batchSave")
	public R batchSave(@RequestBody AuditDTO auditDTO) {
		return R.ok(this.auditService.batchSave(auditDTO));
	}

	@PostMapping(value = "/page")
	public R<PageSupport<AuditDTO>> page(@RequestBody AuditQuery auditQuery) {
		return R.ok(this.auditService.page(auditQuery));
	}

	@PostMapping(value = "/wallet/isAudit")
	public R<List<AuditDTO>> checkWalletDetails(@RequestBody List<Long> commonId) {
		return R.ok(auditService.checkWalletDetails(commonId));
	}


}
