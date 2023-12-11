/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.excel.annotation.ExportExcel;
import com.legendshop.order.dto.ShopBillPeriodDTO;
import com.legendshop.order.excel.ShopBillPeriodExportDTO;
import com.legendshop.order.query.ShopBillPeriodQuery;
import com.legendshop.order.service.ShopBillPeriodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@Tag(name = "结算账单")
@RestController
@RequestMapping(value = "/admin/bill/period", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminShopBillPeriodController {

	@Autowired
	private ShopBillPeriodService shopBillPeriodService;


	@Operation(summary = "【后台】结算档期分页查询", description = "")
	@PreAuthorize("@pms.hasPermission('admin_bill_period_page_queryShopBillPeriod')")
	@GetMapping("/page")
	public R<PageSupport<ShopBillPeriodDTO>> queryShopBillPeriod(ShopBillPeriodQuery shopBillPeriodQuery) {
		return R.ok(shopBillPeriodService.queryShopBillPeriod(shopBillPeriodQuery));
	}


	@ExportExcel(name = "结算档期列表", sheet = "结算档期列表")
	@Operation(summary = "【后台】结算档期列表导出", description = "")
	@PreAuthorize("@pms.hasPermission('admin_bill_period_export')")
	@GetMapping("/export")
	public List<ShopBillPeriodExportDTO> exportShopBillPeriod(ShopBillPeriodQuery shopBillPeriodQuery) {
		return shopBillPeriodService.exportShopBillPeriod(shopBillPeriodQuery);
	}
}
