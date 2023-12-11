/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.controller;

import com.legendshop.common.core.constant.R;
import com.legendshop.order.dto.OrderItemDTO;
import com.legendshop.order.service.OrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@RestController
@AllArgsConstructor
public class OrderItemController {

	private final OrderItemService orderItemService;


	@GetMapping("/item/{id}")
	public R<OrderItemDTO> getById(@PathVariable("id") Long id) {
		return R.ok(orderItemService.getById(id));
	}

	@PostMapping("/item/queryById")
	public R<List<OrderItemDTO>> queryById(@RequestBody List<Long> ids) {
		return R.ok(orderItemService.queryById(ids));
	}

	@PostMapping("/item/updateOrderItemCommFlag")
	public R<Boolean> updateOrderItemCommFlag(@RequestParam("status") Integer status, @RequestParam("orderItemId") Long orderItemId, @RequestParam("userId") Long userId) {
		return R.ok(orderItemService.updateOrderItemCommFlag(status, orderItemId, userId));
	}

	@PostMapping("/item/update")
	public R<Integer> update(List<OrderItemDTO> orderItemDTOS) {
		return R.ok(orderItemService.update(orderItemDTOS));
	}

	@GetMapping("/item/queryByUserId")
	public R<List<OrderItemDTO>> queryByUserId(@RequestParam("userId") Long userId) {
		return R.ok(orderItemService.queryByUserId(userId));
	}

	@GetMapping("/item/queryByOrderNumber")
	public R<List<OrderItemDTO>> queryByOrderNumber(@RequestParam("orderNumber") String orderNumber) {
		return R.ok(orderItemService.queryByOrderNumber(orderNumber));
	}


	@PostMapping("/item/queryByNumberList")
	public R<List<OrderItemDTO>> queryByNumberList(@RequestBody List<String> numberList) {
		return this.orderItemService.queryByNumberList(numberList);
	}
}
