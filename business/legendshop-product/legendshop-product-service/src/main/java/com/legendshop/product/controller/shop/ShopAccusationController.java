/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.shop;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.excel.annotation.ExportExcel;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.bo.AccusationBO;
import com.legendshop.product.bo.IllegalProductBO;
import com.legendshop.product.excel.IllegalExportDTO;
import com.legendshop.product.query.AccusationQuery;
import com.legendshop.product.query.ProductQuery;
import com.legendshop.product.service.AccusationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@Tag(name = "商品举报管理")
@RestController
@RequestMapping(value = "/s/accusation", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ShopAccusationController {

	@Autowired
	private AccusationService accusationService;

	/**
	 * 举报商品列表查询
	 */
	@GetMapping("/page")
	@Operation(summary = "【商家】举报商品列表查询")
	@PreAuthorize("@pms.hasPermission('s_product_accusation_page')")
	public R<PageSupport<AccusationBO>> accusationPage(AccusationQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(accusationService.shopPage(query));
	}

	/**
	 * 违规商品导出
	 */
	@GetMapping("/illegalExport")
	@Operation(summary = "【商家】违规商品导出")
	@PreAuthorize("@pms.hasPermission('s_product_accusation_export')")
	@ExportExcel(name = "违规商品列表", sheet = "违规商品列表")
	public List<IllegalExportDTO> illegalExport(ProductQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return accusationService.findIllegalExport(query);
	}

	/**
	 * 违规商品分页查询
	 */
	@GetMapping("/illegalPage")
	@PreAuthorize("@pms.hasPermission('s_product_accusation_page')")
	@Operation(summary = "【商家】违规商品分页查询")
	public R<PageSupport<IllegalProductBO>> illegalPage(ProductQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(accusationService.illegalPage(query));
	}

	/**
	 * 举报商品列表查询
	 */
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('s_product_accusation_get')")
	@Parameter(name = "id", description = "举报ID", required = true)
	@Operation(summary = "【商家】举报商品列表查询")
	public R<AccusationBO> getAccusationById(@PathVariable Long id) {
		return R.ok(accusationService.getAccusation(id));
	}
}
