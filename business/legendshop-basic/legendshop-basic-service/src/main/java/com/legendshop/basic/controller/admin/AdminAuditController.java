/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.bo.PendingMattersAdminBO;
import com.legendshop.basic.dto.AuditDTO;
import com.legendshop.basic.enums.AuditTypeEnum;
import com.legendshop.basic.query.AuditQuery;
import com.legendshop.basic.service.AuditService;
import com.legendshop.common.core.constant.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@Tag(name = "后台审核管理")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin/audit")
public class AdminAuditController {

	final AuditService auditService;

	/**
	 * 后台获取商品的审核历史
	 *
	 * @param productId
	 * @return
	 */
	@Operation(summary = "【后台】后台商品审核管理")
	@Parameters({
			@Parameter(name = "productId", description = "商品Id", required = true),
			@Parameter(name = "curPage", description = "当前页", required = true),
			@Parameter(name = "pageSize", description = "每页大小", required = true)
	})
	@GetMapping(value = "/getAuditByProductId")
	public R<PageSupport<AuditDTO>> getAuditByProductId(@RequestParam(value = "productId") Long productId,
														@RequestParam(value = "curPage") Integer curPage,
														@RequestParam(value = "pageSize") Integer pageSize) {
		AuditQuery query = new AuditQuery();
		query.setCommonId(productId);
		query.setAuditType(AuditTypeEnum.PRODUCT.getValue());
		query.setCurPage(curPage);
		query.setPageSize(pageSize);
		return R.ok(this.auditService.page(query));
	}

	@GetMapping(value = "/getAuditDTOSByAuditTypeAndCommonId")
	public R<List<AuditDTO>> getAuditDtoListByAuditTypeAndCommonId(@RequestParam(value = "commonId", required = true) Long commonId,
																   @RequestParam(value = "auditType", required = true) Integer auditType) {
		return R.ok(auditService.getAuditDtoListByAuditTypeAndCommonId(commonId, auditType));
	}

	@Operation(summary = "【后台】首页待处理事项")
	@GetMapping(value = "/getPending")
	public R<PendingMattersAdminBO> pending() {
		return R.ok(auditService.getPending());
	}


}
