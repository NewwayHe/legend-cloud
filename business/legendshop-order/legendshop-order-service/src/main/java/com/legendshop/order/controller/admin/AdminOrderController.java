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
import com.legendshop.order.bo.OrderBO;
import com.legendshop.order.dto.OrderDTO;
import com.legendshop.order.query.OrderSearchQuery;
import com.legendshop.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 平台端订单管理
 *
 * @author legendshop
 */
@Slf4j
@Tag(name = "订单管理")
@RestController
@RequestMapping(value = "/admin/order", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminOrderController {

	@Autowired
	private OrderService orderService;


	@PreAuthorize("@pms.hasPermission('admin_order_page')")
	@Operation(summary = "【后台】订单分页列表", description = "")
	@GetMapping("/page")
	public R<PageSupport<OrderBO>> queryOrderWithItem(OrderSearchQuery query) {
		return R.ok(orderService.queryOrderWithItem(query));
	}


	@PreAuthorize("@pms.hasPermission('admin_order_get')")
	@Operation(summary = "【后台】查看订单详情", description = "")
	@Parameter(name = "orderId", description = "订单id", required = true)
	@GetMapping("/get")
	public R<OrderBO> get(@RequestParam Long orderId) {
		return R.ok(orderService.getAdminOrderDetail(orderId));
	}


	@PreAuthorize("@pms.hasPermission('admin_order_bill_page')")
	@Operation(summary = "【后台】账单详情里的订单分页列表", description = "")
	@GetMapping("/bill/page")
	public R<PageSupport<OrderDTO>> queryBillOrderList(OrderSearchQuery query) {
		Assert.hasLength(query.getBillSn(), "结算单号不能为空！！！");
		return R.ok(orderService.queryBillOrderList(query));
	}

	@PreAuthorize("@pms.hasPermission('admin_order_export')")
	@Operation(summary = "【后台】订单数据导出", description = "")
	@GetMapping("/export")
	public R<Void> orderExport(OrderSearchQuery query) throws IOException {
		return orderService.orderExport(query);
	}


	@Operation(summary = "【后台】添加备注", description = "")
	@Parameters({
			@Parameter(name = "orderNumber", description = "订单号", required = true),
			@Parameter(name = "remark", description = "备注内容", required = true)
	})
	@PostMapping("/insert/remake")
	public R insertRemark(@RequestParam String orderNumber, @RequestParam String remark) {
		if (orderService.insertRemark(orderNumber, remark)) {
			return R.ok();
		}
		return R.fail("操作失败");
	}

}
