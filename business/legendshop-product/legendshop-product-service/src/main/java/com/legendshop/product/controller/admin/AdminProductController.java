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
import com.legendshop.basic.dto.AuditDTO;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.excel.annotation.ExportExcel;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.dto.AdminUserDetail;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.bo.ProductAccusationBO;
import com.legendshop.product.bo.ProductAuditBO;
import com.legendshop.product.bo.ProductBO;
import com.legendshop.product.bo.ProductNewCountBO;
import com.legendshop.product.dto.ProductBatchDelDTO;
import com.legendshop.product.dto.ProductBranchDTO;
import com.legendshop.product.dto.ProductPlatformExportDTO;
import com.legendshop.product.enums.ProductStatusEnum;
import com.legendshop.product.excel.ProductRecycleBinExportDTO;
import com.legendshop.product.query.ProductQuery;
import com.legendshop.product.service.ProductService;
import com.legendshop.product.utils.ProductStatusChecker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品管理
 *
 * @author legendshop
 */
@Tag(name = "商品管理")
@RestController
@RequestMapping(value = "/admin/product", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AdminProductController {

	private final ProductService productService;

	private final ProductStatusChecker productStatusChecker;

	/**
	 * 批量更新状态
	 *
	 * @param productDTO
	 * @return
	 */
	@PutMapping("/batchUpdateStatus")
	@SystemLog("批量更新商品状态")
	@PreAuthorize("@pms.hasPermission('admin_product_product_batchUpdateStatus')")
	@Operation(summary = "【后台】批量更新商品状态")
	public R batchUpdateStatus(@RequestBody @Valid ProductBranchDTO productDTO) {
		return productService.batchUpdateStatus(productDTO);
	}

	/**
	 * 批量更新商品删除状态
	 *
	 * @param productDTO
	 * @return
	 */
	@PutMapping("/batchUpdateDelStatus")
	@SystemLog("批量更新商品删除状态")
	@PreAuthorize("@pms.hasPermission('admin_product_product_batchUpdateDelStatus')")
	@Operation(summary = "【后台】批量更新商品删除状态")
	public R batchUpdateDelStatus(@RequestBody @Valid ProductBatchDelDTO productDTO) {
		return productService.batchUpdateDelStatus(productDTO, null);
	}

	/**
	 * 审核
	 *
	 * @param auditDTO
	 * @return
	 */
	@PutMapping("/audit")
	@SystemLog("审核商品")
	@Operation(summary = "【后台】审核商品")
	@PreAuthorize("@pms.hasPermission('admin_product_product_audit')")
	public R audit(@Valid @RequestBody AuditDTO auditDTO) {
		auditDTO.setAuditUsername(SecurityUtils.getAdminUser().getUsername());
		return productService.audit(auditDTO);
	}

	/**
	 * 分页查询
	 *
	 * @param query
	 * @return
	 */
	@GetMapping("/page")
	@Operation(summary = "【后台】商品分页查询")
	public R<PageSupport<ProductBO>> page(ProductQuery query) {
		return R.ok(this.productService.getProductPage(query));
	}

	/**
	 * 分页查询
	 *
	 * @param query
	 * @return
	 */
	@GetMapping("/auditPage")
	@Operation(summary = "【后台】商品审核分页查询")
	public R<PageSupport<ProductAuditBO>> auditPage(ProductQuery query) {
		return R.ok(this.productService.auditPage(query));
	}

	/**
	 * 分页查询
	 *
	 * @param query
	 * @return
	 */
	@GetMapping("/accusationPage")
	@Operation(summary = "【后台】商品违规分页查询")
	public R<PageSupport<ProductAccusationBO>> accusationPage(ProductQuery query) {
		return R.ok(productService.accusationPage(query));
	}

	/**
	 * 分页查询
	 *
	 * @param auditDTO
	 * @return
	 */
	@PostMapping("/productOnline")
	@PreAuthorize("@pms.hasPermission('admin_product_product_productOnline')")
	@Operation(summary = "【后台】违规商品上架 admin_product_product_productOnline")
	public R productOnline(@RequestBody AuditDTO auditDTO) {
		auditDTO.setAuditUsername(SecurityUtils.getAdminUser().getUsername());
		return productService.productOnline(auditDTO);
	}

	/**
	 * 导出excel商品信息
	 *
	 * @param query
	 * @return
	 */
	@GetMapping(value = "/export")
	@PreAuthorize("@pms.hasPermission('admin_product_product_export')")
	@Operation(summary = "【后台】导出商品信息")
	@ExportExcel(name = "商品列表", sheet = "商品列表")
	public List<ProductPlatformExportDTO> export(@Valid ProductQuery query) {
		return productService.findExportPlatformProd(query);
	}

	/**
	 * 导出excel商品信息
	 *
	 * @param query
	 * @return
	 */
	@GetMapping(value = "/recycleBin/export")
	@PreAuthorize("@pms.hasPermission('admin_product_product_export')")
	@Operation(summary = "【后台】导出商品回收站信息")
	@ExportExcel(name = "商品回收站列表", sheet = "商品回收站列表")
	public List<ProductRecycleBinExportDTO> exportRecycleBin(@Valid ProductQuery query) {
		return productService.findExportRecycleBinProd(query);
	}

	/**
	 * 商品阅览前需要设置token
	 *
	 * @param productId 商品Id
	 * @return
	 */
	@GetMapping("/preview")
	@Operation(summary = "【后台】商品预览")
	@Parameter(name = "productId", description = "商品id", required = true)
	public R<String> preview(@Valid @NonNull Long productId) {
		AdminUserDetail adminUserDetail = SecurityUtils.getAdminUser();
		String key = adminUserDetail.getUserId() + ":" + productId;
		String token = productStatusChecker.setToken(key);
		return R.ok(key + ":" + token);
	}

	@GetMapping("/activity/page")
	@Operation(summary = "【商家】商品营销活动分页查询（营销活动选择商品弹框）")
	public R<PageSupport<ProductBO>> activityPage(ProductQuery query) {
		Assert.notNull(query.getActivityBegTime(), "活动起始时间不能为空");
		Assert.notNull(query.getActivityEndTime(), "活动结束时间不能为空");
		Assert.notNull(query.getShopId(), "店铺id不能为空");
		query.setStatus(ProductStatusEnum.PROD_ONLINE.getValue());
		query.setOpStatus(OpStatusEnum.PASS.getValue());
		return R.ok(productService.queryActivityInfoPage(query));
	}

	@GetMapping("/index/info")
	@Operation(summary = "【后台】首页商品信息")
	public R<ProductNewCountBO> indexInfo() {
		return R.ok(productService.getIndexInfo());
	}

}
