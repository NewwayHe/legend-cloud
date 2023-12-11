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
import com.legendshop.order.dto.ShopOrderBillDTO;
import com.legendshop.order.excel.ShopOrderBillExportDTO;
import com.legendshop.order.query.ShopOrderBillOrderQuery;
import com.legendshop.order.query.ShopOrderBillQuery;
import com.legendshop.order.service.ShopOrderBillService;
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
 * @author legendshop
 */
@Tag(name = "商家账单结算")
@RestController
@RequestMapping(value = "/s/shop/order/bill", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopOrderBillController {

	@Autowired
	private ShopOrderBillService shopOrderBillService;


	@Operation(summary = "【商家】账单结算分页查询", description = "")
	@PreAuthorize("@pms.hasPermission('s_order_bill_page')")
	@GetMapping("/page")
	public R<PageSupport<ShopOrderBillDTO>> page(ShopOrderBillQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(shopOrderBillService.getShopOrderBillPage(query));
	}


	@Operation(summary = "【商家】账单详情查询", description = "")
	@PreAuthorize("@pms.hasPermission('s_shop_order_bill_get')")
	@GetMapping("/{id}")
	public R<ShopOrderBillDTO> get(@PathVariable Long id) {
		ShopOrderBillDTO shopOrderBillDTO = shopOrderBillService.getById(id);
		if (!SecurityUtils.getShopUser().getShopId().equals(shopOrderBillDTO.getShopId())) {
			return R.fail("非法操作");
		}
		return R.ok(shopOrderBillDTO);
	}

	@ExportExcel(name = "shopOrderBillList", sheet = "结算账单列表")
	@Operation(summary = "【商家】结算账单列表导出", description = "")
	@PreAuthorize("@pms.hasPermission('s_shop_order_bill_export')")
	@GetMapping("/export")
	public List<ShopOrderBillExportDTO> exportShopOrderBill(ShopOrderBillQuery query) {
		return shopOrderBillService.exportShopBillPeriod(query);
	}

	@PreAuthorize("@pms.hasPermission('s_shop_order_bill_confirm')")
	@Operation(summary = "【商家】账单确认", description = "")
	@Parameter(name = "id", description = "账单id", required = true)
	@PutMapping("/confirm/{id}")
	public R<Void> updateStatus(@PathVariable Long id) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return shopOrderBillService.shopConfirm(id, shopId);
	}

	@GetMapping("/orderPage")
	@PreAuthorize("@pms.hasPermission('s_shop_order_bill_orderPage')")
	@Operation(summary = "【商家】账单结算订单分页查询", description = "")
	public R<PageSupport> orderPage(@Valid ShopOrderBillOrderQuery shopOrderBillOrderQuery) {
		shopOrderBillOrderQuery.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(shopOrderBillService.getShopOrderBillOrderPage(shopOrderBillOrderQuery));
	}

	@GetMapping("/orderPage/export")
	@ExportExcel(name = "orderList", sheet = "订单列表")
	@PreAuthorize("@pms.hasPermission('s_shop_order_bill_orderPage_export')")
	@Operation(summary = "【商家】账单结算订单导出", description = "")
	public List exportOrderPage(@Valid ShopOrderBillOrderQuery shopOrderBillOrderQuery) {
		shopOrderBillOrderQuery.setShopId(SecurityUtils.getShopUser().getShopId());
		return shopOrderBillService.exportOrderPage(shopOrderBillOrderQuery);
	}

	@GetMapping("/current/bill")
	@Operation(summary = "【商家】首页当前账单结算订单", description = "")
	public R<ShopOrderBillDTO> currentBill() {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return R.ok(shopOrderBillService.getShopOrderBillCount(shopId));
	}
}
