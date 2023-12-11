/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.controller.shop;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.dto.SysParamItemDTO;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.dto.ShopUserDetail;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.order.bo.*;
import com.legendshop.order.dto.LogisticsCompanyDTO;
import com.legendshop.order.query.OrderSearchQuery;
import com.legendshop.order.service.LogisticsCompanyService;
import com.legendshop.order.service.OrderService;
import com.legendshop.order.service.PreSellOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商家端订单管理
 *
 * @author legendshop
 */
@Tag(name = "订单管理")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/s/order", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopOrderController {

	final OrderService orderService;
	final SysParamsApi sysParamsApi;
	final PreSellOrderService preSellOrderService;
	final LogisticsCompanyService logisticsCompanyService;


	@PreAuthorize("@pms.hasPermission('s_order_page')")
	@Operation(summary = "【商家】订单分页列表", description = "")
	@GetMapping("/page")
	public R<PageSupport<OrderBO>> queryOrderWithItem(OrderSearchQuery query) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		query.setShopId(shopId);
		return R.ok(this.orderService.queryOrderWithItem(query));
	}

	@PreAuthorize("@pms.hasPermission('s_order_get')")
	@Operation(summary = "【商家】查看订单详情", description = "")
	@Parameter(name = "orderId", description = "订单id", required = true)
	@GetMapping("/get")
	public R<OrderBO> get(@RequestParam Long orderId) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		OrderBO shopOrderDetail = orderService.getShopOrderDetail(orderId, shopId);
		if (ObjectUtil.isEmpty(shopOrderDetail.getId())) {
			return R.fail(shopOrderDetail, "找不到订单");
		}
		return R.ok(shopOrderDetail);
	}

	@PreAuthorize("@pms.hasPermission('s_order_insertRemark')")
	@Operation(summary = "【商家】添加备注", description = "")
	@Parameters({
			@Parameter(name = "orderNumber", description = "订单流水号", required = true),
			@Parameter(name = "remark", description = "备注内容", required = true)
	})
	@PostMapping("/insert/remake")
	public R insertRemark(@RequestParam String orderNumber, @RequestParam String remark) {
		Assert.isTrue(remark.length() < 200, "备注字数过长!");
		Long shopId = SecurityUtils.getShopUser().getShopId();
		if (orderService.insertRemark(orderNumber, remark, shopId)) {
			return R.ok();
		}
		return R.fail("操作失败");
	}

	@PreAuthorize("@pms.hasPermission('s_order_get_logistics_company')")
	@Operation(summary = "【商家】获取物流公司列表", description = "")
	@GetMapping("/get/logistics/company")
	public R<List<LogisticsCompanyDTO>> getLogisticsCompany() {
		return R.ok(logisticsCompanyService.getList(SecurityUtils.getShopUser().getShopId()));
	}

	@PreAuthorize("@pms.hasPermission('s_order_updateLogistics')")
	@Operation(summary = "【商家】确认发货", description = "")
	@Parameters({
			@Parameter(name = "orderNumber", description = "订单流水号", required = true),
			@Parameter(name = "logisticsNumber", description = "物流单号", required = true),
			@Parameter(name = "logisticsCompanyId", description = "物流公司ID", required = true)
	})
	@PostMapping("/insert/logistic")
	public R updateLogistics(@RequestParam String orderNumber, @RequestParam String logisticsNumber, @RequestParam Long logisticsCompanyId) {
		return orderService.updateLogistics(orderNumber, logisticsCompanyId, logisticsNumber, SecurityUtils.getShopUser().getShopId());
	}


	@PreAuthorize("@pms.hasPermission('s_order_cancel_reason')")
	@Operation(summary = "【商家】取消原因", description = "")
	@GetMapping("/cancel/reason")
	public R<List<SysParamItemDTO>> cancelReason() {
		return R.ok(sysParamsApi.getSysParamItemsByParamName(SysParamNameEnum.ORDER_CANCEL_REASON.name()).getData());
	}

	@PreAuthorize("@pms.hasPermission('s_order_cancelOrder')")
	@Operation(summary = "【商家】取消订单", description = "")
	@PostMapping("/cancel/order")
	public R<List<SysParamItemDTO>> cancelOrder(@Valid @RequestBody OrderCancelBO orderCancelBO) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		if (orderService.cancelOrder(orderCancelBO.getOrderNumbers(), orderCancelBO.getReason(), shopId)) {
			return R.ok();
		}
		return R.fail("操作失败");
	}


	@PreAuthorize("@pms.hasPermission('s_order_changeOrderFee')")
	@Operation(summary = "【商家】调整订单", description = "")
	@Parameters({
			@Parameter(name = "orderNumber", description = "订单流水号", required = true),
			@Parameter(name = "freight", description = "运费", required = true),
			@Parameter(name = "orderAmount", description = "应付金额", required = true)
	})
	@PostMapping("/change/order/fee")
	public R changeOrderFee(@RequestParam String orderNumber, @RequestParam Double freight, @RequestParam Double orderAmount) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		if (orderService.updateOrderPrice(orderNumber, BigDecimal.valueOf(freight), BigDecimal.valueOf(orderAmount), shopId)) {
			return R.ok();
		}
		return R.fail("操作失败");
	}

	/**
	 * 商家主动取消预售订单
	 */
	@PreAuthorize("@pms.hasPermission('s_order_cancelPreSellOrder')")
	@Operation(summary = "【商家】商家主动取消预售订单", description = "")
	@Parameters({
			@Parameter(name = "orderId", description = "订单Id", required = true),
			@Parameter(name = "reason", description = "取消原因", required = true)
	})
	@PostMapping("/cancel/pre/sell/order")
	public R<Void> shopActiveCancelPreSellOrder(@RequestBody Map<String, String> map) {
		long orderId = Long.parseLong(map.get("orderId"));
		String reason = map.get("reason");
		Long shopId = SecurityUtils.getShopUser().getShopId();
		if (null == shopId) {
			return R.fail("请登录~");
		}
		return this.preSellOrderService.shopActiveCancelPreSellOrder(shopId, orderId, reason);
	}

	@PreAuthorize("@pms.hasPermission('s_order_export')")
	@Operation(summary = "【商家】订单数据导出", description = "")
	@GetMapping("/export")
	public R<Void> orderExport(OrderSearchQuery query) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		query.setShopId(shopId);
		return orderService.orderExport(query);
	}


	@Operation(summary = "【商家】首页订单信息", description = "")
	@GetMapping("/index/order/info")
	public R<OrderInfoCountsBO> getIndexOrder() {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return R.ok(this.orderService.getShopOrderCount(shopId));
	}

	@Operation(summary = "【商家】首页支付订单数量", description = "")
	@GetMapping("/index/paidOrder")
	public R<PaidOrderToDayBO> getIndexPaidOrder() {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return R.ok(this.orderService.getShopPaidOrder(shopId));
	}

	@Operation(summary = "【商家】首页待处理事项", description = "")
	@GetMapping("/index/pending")
	public R<PendingMattersShopBO> getPending() {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return R.ok(this.orderService.getPending(shopId));
	}

	@Operation(summary = "【商家】首页销售额-数据", description = "")
	@GetMapping("/indexSales")
	public R<List<PaidOrderCountsBO>> getindexSales() {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return R.ok(this.orderService.getShopSales(shopId));
	}


	@PostMapping("/insert/batch/logistic")
	@PreAuthorize("@pms.hasPermission('s_order_insert_batch_logistic')")
	@Operation(summary = "【商家】批量发货", description = "")
	public R<Void> batchInsertLogistics(@RequestParam("file") MultipartFile file) throws IOException {
		ShopUserDetail shopUser = SecurityUtils.getShopUser();
		return orderService.batchInsertLogistics(shopUser.getShopId(), shopUser.getUsername(), file);
	}


	@Operation(summary = "【商家】修改物流信息", description = "")
	@Parameters({
			@Parameter(name = "id", description = "订单物流信息id", required = true),
			@Parameter(name = "orderNumber", description = "订单流水号", required = true),
			@Parameter(name = "logisticsNumber", description = "物流单号", required = true),
			@Parameter(name = "logisticsCompanyId", description = "物流公司ID", required = true)
	})
	@PostMapping("/update/logistic")
	public R<String> updateLogisticsCompany(@RequestParam Long id, @RequestParam String orderNumber, @RequestParam String logisticsNumber, @RequestParam Long logisticsCompanyId) {
		return orderService.updateLogisticsCompanyOrder(id, orderNumber, logisticsCompanyId, logisticsNumber, SecurityUtils.getShopUser().getShopId());
	}
}
