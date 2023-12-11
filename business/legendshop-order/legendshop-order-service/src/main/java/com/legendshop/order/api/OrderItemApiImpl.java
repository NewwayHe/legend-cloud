/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.order.dto.OrderItemDTO;
import com.legendshop.order.service.OrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@RestController
@AllArgsConstructor
public class OrderItemApiImpl implements OrderItemApi {

	private final OrderItemService orderItemService;

	@Override
	public R<OrderItemDTO> getById(@PathVariable("id") Long id) {
		return R.ok(orderItemService.getById(id));
	}

	@Override
	public R<List<OrderItemDTO>> queryById(@RequestBody List<Long> ids) {
		return R.ok(orderItemService.queryById(ids));
	}

	@Override
	public R<Boolean> updateOrderItemCommFlag(@RequestParam("status") Integer status, @RequestParam("orderItemId") Long orderItemId, @RequestParam("userId") Long userId) {
		return R.ok(orderItemService.updateOrderItemCommFlag(status, orderItemId, userId));
	}

	@Override
	public R<Integer> update(List<OrderItemDTO> orderItemDTOS) {
		return R.ok(orderItemService.update(orderItemDTOS));
	}

	@Override
	public R<List<OrderItemDTO>> queryByUserId(@RequestParam("userId") Long userId) {
		return R.ok(orderItemService.queryByUserId(userId));
	}

	@Override
	public R<List<OrderItemDTO>> queryByOrderNumber(@RequestParam("orderNumber") String orderNumber) {
		return R.ok(orderItemService.queryByOrderNumber(orderNumber));
	}


	@Override
	public R<List<OrderItemDTO>> queryByNumberList(@RequestBody List<String> numberList) {
		return this.orderItemService.queryByNumberList(numberList);
	}

}
