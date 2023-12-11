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
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.bo.BrandBO;
import com.legendshop.product.dto.BrandDTO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.excel.BrandExportDTO;
import com.legendshop.product.query.BrandQuery;
import com.legendshop.product.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 品牌管理（
 *
 * @author legendshop
 */
@Tag(name = "品牌管理")
@RestController
@RequestMapping(value = "/admin/brand", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminBrandController {

	@Autowired
	private BrandService brandService;

	/**
	 * 查询所有的在线品牌
	 */
	@GetMapping("/allOnline")
	@Operation(summary = "【后台】查询所有在线的品牌")
	@PreAuthorize("@pms.hasPermission('admin_product_brand_listAllOnline')")
	public R<List<BrandBO>> queryAllOnline(String brandName) {
		return R.ok(brandService.getAllOnline(0L, brandName));
	}

	/**
	 * 分页查询
	 */
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('admin_product_brand_page')")
	@Operation(summary = "【后台】品牌分页查询")
	public R<PageSupport<BrandBO>> page(BrandQuery query) {
		return R.ok(brandService.queryPage(query));
	}

	/**
	 * 根据id删除
	 */
	@DeleteMapping("/{id}")
	@SystemLog("根据id删除品牌")
	@Parameter(name = "id", description = "品牌ID", required = true)
	@Operation(summary = "【后台】根据id删除品牌")
	@PreAuthorize("@pms.hasPermission('admin_product_brand_del')")
	public R delById(@PathVariable("id") Long id) {
		return brandService.deleteById(id);
	}

	/**
	 * 根据id查询
	 */
	@GetMapping("/{id}")
	@Parameter(name = "id", description = "品牌ID", required = true)
	@Operation(summary = "【后台】根据id查询")
	@PreAuthorize("@pms.hasPermission('admin_product_brand_get')")
	public R<BrandBO> getById(@PathVariable("id") Long id) {
		return R.ok(brandService.getById(id));
	}

	/**
	 * 保存
	 */
	@PostMapping
	@Operation(summary = "【后台】添加品牌")
	@SystemLog("添加品牌")
	@PreAuthorize("@pms.hasPermission('admin_product_brand_add')")
	public R<String> save(@Valid @RequestBody BrandDTO brandDTO) {
		//跳过审核
		brandDTO.setOpStatus(OpStatusEnum.PASS.getValue());
		return brandService.save(brandDTO);
	}

	/**
	 * 更新
	 */
	@PutMapping
	@SystemLog("更新品牌")
	@Operation(summary = "【后台】更新品牌")
	@PreAuthorize("@pms.hasPermission('admin_product_brand_update')")
	public R update(@Valid @RequestBody BrandDTO brandDTO) {
		return brandService.update(brandDTO);
	}

	/**
	 * 更新品牌状态
	 */
	@SystemLog("更新品牌状态")
	@PutMapping("/updateStatus")
	@Operation(summary = "【后台】更新品牌状态")
	@PreAuthorize("@pms.hasPermission('admin_product_brand_updateStatus')")
	public R<Integer> updateStatus(@RequestBody ProductDTO productDTO) {
		return brandService.updateStatus(productDTO.getStatus(), productDTO.getId());
	}

	/**
	 * 审核
	 */
	@PutMapping("/audit")
	@SystemLog("品牌审核")
	@Operation(summary = "【后台】品牌审核")
	@PreAuthorize("@pms.hasPermission('admin_product_brand_audit')")
	public R audit(@Valid @RequestBody AuditDTO auditDTO) {
		auditDTO.setAuditUsername(SecurityUtils.getAdminUser().getUsername());
		return brandService.audit(auditDTO);
	}

	/**
	 * 品牌管理：导出
	 *
	 * @param query
	 * @return
	 */
	@GetMapping("/export")
	@Operation(summary = "【后台】导出品牌")
	@PreAuthorize("@pms.hasPermission('admin_product_brand_export')")
	@ExportExcel(name = "品牌列表", sheet = "品牌列表")
	public List<BrandExportDTO> Export(BrandQuery query) {
		return this.brandService.getExportBrands(query);
	}
}
