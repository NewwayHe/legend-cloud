/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.controller.shop;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.excel.annotation.ExportExcel;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.order.dto.OrderInvoiceDTO;
import com.legendshop.order.excel.OrderInvoiceExportDTO;
import com.legendshop.order.query.OrderInvoiceQuery;
import com.legendshop.order.service.OrderInvoiceService;
import com.legendshop.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@RestController
@RequestMapping(value = "/s/order/invoice", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "订单发票")
public class ShopOrderInvoiceController {

	@Resource
	private OrderInvoiceService orderInvoiceService;

	@Autowired
	private OrderService orderService;

	@Operation(summary = "【商家】订单发票分页列表", description = "")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('s_order_invoice_page')")
	public R<PageSupport<OrderInvoiceDTO>> queryPage(OrderInvoiceQuery orderInvoiceQuery) {
		orderInvoiceQuery.setShopId(SecurityUtils.getShopUser().getShopId());
		PageSupport<OrderInvoiceDTO> page = orderInvoiceService.queryPage(orderInvoiceQuery);
		return R.ok(page);
	}


	@Operation(summary = "【商家】开票", description = "")
	@PostMapping("/invoicing")
	@PreAuthorize("@pms.hasPermission('s_order_invoice_invoicing')")
	public R<Void> invoicing(String orderNumber) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return orderService.invoicing(shopId, orderNumber);
	}


	@Operation(summary = "【商家】批量开票", description = "")
	@PostMapping("/batch/invoicing")
	@PreAuthorize("@pms.hasPermission('s_order_invoice_batch_invoicing')")
	public R<Void> batchInvoicing(@RequestBody List<String> ids) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return orderService.batchInvoicing(shopId, ids);
	}


	@PreAuthorize("@pms.hasPermission('s_order_invoice_export')")
	@Operation(summary = "【商家】订单发票数据导出", description = "")
	@GetMapping("/export")
	@ExportExcel(name = "订单发票列表", sheet = "订单发票列表")
	public List<OrderInvoiceExportDTO> orderInvoiceExport(OrderInvoiceQuery query) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		query.setShopId(shopId);
		return orderInvoiceService.orderInvoiceExport(query);
	}

}
