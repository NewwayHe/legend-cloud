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
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.order.dto.OrderItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "orderItemApi", value = ServiceNameConstants.ORDER_SERVICE)
public interface OrderItemApi {

	String PREFIX = ServiceNameConstants.ORDER_SERVICE_RPC_PREFIX;

	@GetMapping(PREFIX + "/item/{id}")
	R<OrderItemDTO> getById(@PathVariable("id") Long id);

	@PostMapping(PREFIX + "/item/queryById")
	R<List<OrderItemDTO>> queryById(@RequestBody List<Long> ids);

	@PostMapping(PREFIX + "/item/updateOrderItemCommFlag")
	R<Boolean> updateOrderItemCommFlag(@RequestParam("status") Integer status, @RequestParam("orderItemId") Long orderItemId, @RequestParam("userId") Long userId);

	@PostMapping(PREFIX + "/item/update")
	R<Integer> update(@RequestBody List<OrderItemDTO> orderItemDTOS);

	@GetMapping(PREFIX + "/item/queryByUserId")
	R<List<OrderItemDTO>> queryByUserId(@RequestParam("userId") Long userId);

	@GetMapping(PREFIX + "/item/queryByOrderNumber")
	R<List<OrderItemDTO>> queryByOrderNumber(@RequestParam("orderNumber") String orderNumber);

	@PostMapping(PREFIX + "/item/queryByNumberList")
	R<List<OrderItemDTO>> queryByNumberList(@RequestBody List<String> numberList);
}
