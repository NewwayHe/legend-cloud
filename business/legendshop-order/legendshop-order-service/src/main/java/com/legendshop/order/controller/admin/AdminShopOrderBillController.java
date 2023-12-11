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
import com.legendshop.order.dto.OrderRefundReturnDTO;
import com.legendshop.order.dto.ShopOrderBillDTO;
import com.legendshop.order.enums.PayTypeEnum;
import com.legendshop.order.enums.ShopOrderBillStatusEnum;
import com.legendshop.order.excel.ShopOrderBillExportDTO;
import com.legendshop.order.query.OrderRefundReturnQuery;
import com.legendshop.order.query.ShopOrderBillOrderQuery;
import com.legendshop.order.query.ShopOrderBillQuery;
import com.legendshop.order.service.OrderRefundReturnService;
import com.legendshop.order.service.ShopOrderBillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@Tag(name = "结算账单")
@RestController
@RequestMapping(value = "/admin/shop/order/bill", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminShopOrderBillController {


	@Autowired
	private ShopOrderBillService shopOrderBillService;

	@Autowired
	private OrderRefundReturnService refundReturnService;


	@Operation(summary = "【后台】结算账单分页查询", description = "")
	@PreAuthorize("@pms.hasPermission('admin_shop_order_bill_page')")
	@GetMapping("/page")
	public R<PageSupport<ShopOrderBillDTO>> page(ShopOrderBillQuery query) {
		return R.ok(shopOrderBillService.getShopOrderBillPage(query));
	}


	@Operation(summary = "【后台】账单详情查询", description = "")
	@Parameter(name = "id", description = "账单id", required = true)
	@PreAuthorize("@pms.hasPermission('admin_shop_order_bill_get')")
	@GetMapping("/{id}")
	public R<ShopOrderBillDTO> get(@PathVariable Long id) {
		ShopOrderBillDTO shopOrderBillDTO = shopOrderBillService.getById(id);
		return R.ok(shopOrderBillDTO);
	}

	@Operation(summary = "【后台】账单审核", description = "")
	@Parameter(name = "id", description = "账单id", required = true)
	@PutMapping("/check/{id}")
	public R<Void> updateStatus(@PathVariable Long id) {
		return shopOrderBillService.adminCheck(id);
	}

	@Operation(summary = "【后台】账单结算", description = "")
	@Parameters({
			@Parameter(name = "id", description = "账单id", required = true),
			@Parameter(name = "payDate", description = "支付时间", required = true),
			@Parameter(name = "payContent", description = "支付备注")
	})
	@PostMapping("/pay")
	public R<Void> payBill(@RequestParam Long id, String payDate, String payContent) {
		return shopOrderBillService.payBill(ShopOrderBillStatusEnum.FINISH, id, payDate, payContent, PayTypeEnum.OFFLINE);
	}


	@Operation(summary = "【后台】退款账单分页查询", description = "")
	@PreAuthorize("@pms.hasPermission('admin_shop_order_bill_queryRefundReturnBillPage')")
	@GetMapping("/refund/return/page")
	public R<PageSupport<OrderRefundReturnDTO>> queryRefundReturnBillPage(OrderRefundReturnQuery query) {
		Assert.hasLength(query.getBillSn(), "账单编号billSn不能为空！");
		PageSupport<OrderRefundReturnDTO> page = refundReturnService.getByBillSnPage(query);
		return R.ok(page);
	}


	@ExportExcel(name = "结算账单列表", sheet = "结算账单列表")
	@Operation(summary = "【后台】结算账单列表导出", description = "")
	@PreAuthorize("@pms.hasPermission('admin_shop_order_bill_export')")
	@GetMapping("/export")
	public List<ShopOrderBillExportDTO> exportShopOrderBill(ShopOrderBillQuery query) {
		return shopOrderBillService.exportShopBillPeriod(query);
	}


	@GetMapping("/detailList")
	@PreAuthorize("@pms.hasPermission('admin_shop_order_bill_detailList')")
	@Operation(summary = "【后台】结算账单详情账单分页查询", description = "")
	public R<PageSupport> orderPage(@Valid ShopOrderBillOrderQuery shopOrderBillOrderQuery) {
		return R.ok(shopOrderBillService.getShopOrderBillOrderPage(shopOrderBillOrderQuery));
	}

	@GetMapping("/detailList/export")
	@PreAuthorize("@pms.hasPermission('admin_shop_order_bill_detailList_export')")
	@ExportExcel(name = "orderList", sheet = "结算账单详情账单列表")
	@Operation(summary = "【后台】结算账单详情账单导出", description = "")
	public List exportOrderPage(@Valid ShopOrderBillOrderQuery shopOrderBillOrderQuery) {
		return shopOrderBillService.exportOrderPage(shopOrderBillOrderQuery);
	}
}
