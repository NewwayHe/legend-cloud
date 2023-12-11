/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.product.bo.ProductCommentAnalysisBO;
import com.legendshop.product.bo.ProductCommentInfoBO;
import com.legendshop.product.dto.ProductCommentAuditDTO;
import com.legendshop.product.query.ProductCommentQuery;
import com.legendshop.product.service.ProductAddCommentService;
import com.legendshop.product.service.ProductCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author legendshop
 */
@Tag(name = "商品评论")
@RestController
@RequestMapping(value = "/admin/product/comment", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminProductCommentController {

	@Autowired
	private ProductCommentService productCommentService;

	@Autowired
	private ProductAddCommentService productAddCommentService;

	@PreAuthorize("@pms.hasPermission('admin_product_comment_page')")
	@Operation(summary = "【后台】评论分页查询")
	@GetMapping("/page")
	public R<PageSupport<ProductCommentInfoBO>> queryProductComment(ProductCommentQuery productCommentQuery) {
		return R.ok(productCommentService.getAdminProductCommentList(productCommentQuery));
	}

	@PreAuthorize("@pms.hasPermission('admin_product_comment_delete')")
	@DeleteMapping("/deleteProductComment/{id}")
	@Operation(summary = "【后台】评论删除 admin_product_comment_delete")
	public R<Integer> deleteProductComment(@PathVariable Long id) {
		return R.ok(productCommentService.deleteByProductCommentId(id));
	}

	@PreAuthorize("@pms.hasPermission('admin_product_comment_analysis')")
	@Operation(summary = "【后台】统计分析")
	@GetMapping("/analysis")
	public R<ProductCommentAnalysisBO> productCommentAnalysis() {
		return productCommentService.getAdminProductCommentAnalysis();
	}

	@PreAuthorize("@pms.hasPermission('admin_product_comment_get')")
	@Operation(summary = "【后台】查看评论详情")
	@Parameter(name = "id", description = "评论id", required = true)
	@GetMapping("/get")
	public R<ProductCommentInfoBO> get(@RequestParam Long id) {
		return R.ok(productCommentService.getProductCommentDetail(id));
	}

	@PreAuthorize("@pms.hasPermission('admin_product_comment_auditAddComment')")
	@Operation(summary = "【后台】审核追评")
	@Parameters({
			@Parameter(name = "addId", description = "追评id", required = true),
			@Parameter(name = "status", description = "审核状态  1：通过  -1：拒绝", required = true)
	})
	@PostMapping("/audit/add")
	public R auditAddComment(@RequestParam Long addId, @RequestParam Integer status) {
		if (status != 1 && status != -1) {
			return R.fail("审核状态异常");
		}
		return R.ok(productAddCommentService.auditAddComment(addId, status));
	}

	@PreAuthorize("@pms.hasPermission('admin_productComment_audit')")
	@Operation(summary = "【后台】审核初评")
	@Parameters({
			@Parameter(name = "id", description = "评论id", required = true),
			@Parameter(name = "status", description = "审核状态  1：通过  -1：拒绝", required = true)
	})
	@PostMapping("/audit")
	public R auditFirstComment(@RequestParam Long id, @RequestParam Integer status) {
		if (status != 1 && status != -1) {
			return R.fail("审核状态异常");
		}
		return R.ok(productCommentService.auditFirstComment(id, status));
	}

	@PreAuthorize("@pms.hasPermission('admin_productComment_batchAuditComment')")
	@Operation(summary = "【后台】批量审核初评追评")
	@PostMapping("/batch/audit")
	public R batchAuditComment(@RequestBody ProductCommentAuditDTO productCommentAuditDTO) {
		Integer status = productCommentAuditDTO.getStatus();
		if (status != 1 && status != -1) {
			return R.fail("审核状态异常");
		}
		if (productCommentAuditDTO.getIds().size() == 0 && productCommentAuditDTO.getAddIds().size() == 0) {
			return R.fail("请选择要审核的评论");
		}
		return R.ok(productCommentService.batchAuditComment(productCommentAuditDTO.getIds(), productCommentAuditDTO.getAddIds(), status));
	}

}
