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
import com.legendshop.order.query.OrderSearchQuery;
import com.legendshop.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@Tag(name = "订单管理")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin/user/order", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUserOrderController {

	private final OrderService orderService;

	@GetMapping("/page")
	@Operation(summary = "【后台】用户订单分页列表", description = "")
	public R<PageSupport<OrderBO>> queryUserOrderPage(OrderSearchQuery query) {
		if (null == query.getUserId()) {
			return R.fail("查询用户不存在");
		}
		return R.ok(this.orderService.queryOrderWithItem(query));
	}

}
