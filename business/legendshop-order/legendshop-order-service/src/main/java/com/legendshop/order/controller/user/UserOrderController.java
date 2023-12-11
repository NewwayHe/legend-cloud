/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.controller.user;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.RequestHeaderConstant;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.order.bo.OrderBO;
import com.legendshop.order.dto.OrderItemDTO;
import com.legendshop.order.dto.OrderLogisticsDTO;
import com.legendshop.order.query.OrderItemQuery;
import com.legendshop.order.query.OrderSearchQuery;
import com.legendshop.order.service.OrderItemService;
import com.legendshop.order.service.OrderLogisticsService;
import com.legendshop.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户端订单管理
 *
 * @author legendshop
 */
@Tag(name = "订单管理")
@RestController
@RequestMapping(value = "/p/order", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserOrderController {

	private final OrderService orderService;

	private final OrderItemService orderItemService;

	private final OrderLogisticsService orderLogisticsService;


	@Operation(summary = "【用户】订单分页列表", description = "")
	@GetMapping("/page")
	public R<PageSupport<OrderBO>> queryOrderWithItem(OrderSearchQuery query) {
		Long userId = SecurityUtils.getUserId();
		query.setUserId(userId);
		return R.ok(this.orderService.queryOrderWithItem(query));
	}


	@Operation(summary = "【用户】查看订单详情", description = "")
	@Parameter(name = "orderId", description = "订单id", required = true)
	@GetMapping("/get")
	public R<OrderBO> get(@RequestParam Long orderId) {
		Long userId = SecurityUtils.getUserId();
		return orderService.getUserOrderDetail(orderId, userId);
	}


	@Operation(summary = "【用户】可退款/退货列表", description = "")
	@Parameter(name = "orderId", description = "订单id", required = true)
	@GetMapping("/get/after/sales")
	public R<List<OrderItemDTO>> getAfterSalesList(@RequestParam Long orderId) {
		return R.ok(orderService.getAfterSalesList(orderId));
	}


	@Operation(summary = "【用户】取消订单", description = "")
	@Parameter(name = "orderNumber", description = "订单流水号", required = true)
	@PostMapping("/cancel/order")
	public R<Void> cancelOrder(@RequestParam String orderNumber) {
		Long userId = SecurityUtils.getUserId();
		return orderService.cancelOrder(orderNumber, userId);
	}


	@Operation(summary = "【用户】提醒发货", description = "")
	@Parameters({
			@Parameter(name = "orderNumber", description = "订单流水号", required = true),
			@Parameter(name = "shopId", description = "商家id", required = true)
	})
	@PostMapping("/remind/delivery")
	public R remindDelivery(@RequestParam String orderNumber, @RequestParam Long shopId) {
		if (orderService.remindDeliveryBySn(orderNumber, shopId, SecurityUtils.getUserId()) > 0) {
			return R.ok();
		}
		return R.fail("操作失败");
	}


	@Operation(summary = "【用户】用户确认收货", description = "")
	@Parameter(name = "orderId", description = "订单ID", required = true)
	@PostMapping("/confirm/deliver")
	public R<Void> confirmDeliver(@RequestParam Long orderId) {
		Long userId = SecurityUtils.getUser().getUserId();
		return orderService.confirmDeliver(orderId, userId);
	}


	@Operation(summary = "【用户】再来一单", description = "")
	@Parameter(name = "orderNumber", description = "订单流水号", required = true)
	@PostMapping("/add/another/order")
	public R addAnotherOrder(@RequestParam String orderNumber, HttpServletRequest request) {
		return orderService.addAnotherOrder(orderNumber, SecurityUtils.getUserId(), request.getHeader(RequestHeaderConstant.SOURCE_KEY));
	}


	@Operation(summary = "【用户】删除订单", description = "")
	@Parameter(name = "orderId", description = "订单ID", required = true)
	@DeleteMapping("/{orderId}")
	public R delete(@PathVariable Long orderId) {
		if (orderService.deleteOrder(SecurityUtils.getUserId(), orderId)) {
			return R.ok();
		}
		return R.fail("删除失败");
	}


	@Operation(summary = "【用户】订单项分页列表", description = "")
	@GetMapping("/item/page")
	public R<PageSupport<OrderItemDTO>> queryOrderItem(OrderItemQuery query) {
		Long userId = SecurityUtils.getUserId();
		query.setUserId(userId);
		return R.ok(orderItemService.queryOrderItems(query));
	}


	@Operation(summary = "【用户】订单项商品分页列表", description = "")
	@GetMapping("/item/prod/page")
	public R<PageSupport<OrderItemDTO>> queryOrderItemProd(OrderItemQuery query) {
		Long userId = SecurityUtils.getUserId();
		query.setUserId(userId);
		return R.ok(orderItemService.queryOrderItemsProd(query));
	}


	@Operation(summary = "【用户】查看物流", description = "")
	@Parameter(name = "orderNumber", description = "订单流水号", required = true)
	@PostMapping("/logistics/information")
	public R<OrderLogisticsDTO> logisticsInformation(@RequestParam String orderNumber) {
		return orderLogisticsService.queryLogisticsInformation(orderNumber);
	}


	@Operation(summary = "【用户】修改发货", description = "")
	@Parameters({
			@Parameter(name = "refundId", description = "售后id", required = true),
			@Parameter(name = "id", description = "订单物流信息id", required = true),
			@Parameter(name = "orderNumber", description = "订单流水号", required = true),
			@Parameter(name = "logisticsNumber", description = "物流单号", required = true),
			@Parameter(name = "logisticsCompanyId", description = "物流公司ID", required = true)
	})
	@PostMapping("/update/logistic")
	public R updateLogisticsCompany(@RequestParam Long refundId, @RequestParam Long id, @RequestParam String orderNumber, @RequestParam String logisticsNumber, @RequestParam Long logisticsCompanyId) {
		return orderService.updateLogisticsUser(refundId, id, orderNumber, logisticsCompanyId, logisticsNumber, SecurityUtils.getUser().getUserId());
	}

}
