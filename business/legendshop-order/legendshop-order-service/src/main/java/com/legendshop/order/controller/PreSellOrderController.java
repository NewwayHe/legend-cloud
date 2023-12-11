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
import com.legendshop.order.dto.PreSellOrderDTO;
import com.legendshop.order.service.PreSellOrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/pre-sell/order", produces = MediaType.APPLICATION_JSON_VALUE)
public class PreSellOrderController {

	private final PreSellOrderService preSellOrderService;

	@PutMapping("/update")
	public R<Integer> update(@RequestBody List<PreSellOrderDTO> preSellOrderList) {
		return R.ok(preSellOrderService.update(preSellOrderList));
	}

	@PutMapping("/updateOne")
	public R<Integer> update(@RequestBody PreSellOrderDTO preSellOrderDTO) {
		return R.ok(preSellOrderService.update(preSellOrderDTO));
	}


	@GetMapping("/getByOrderId")
	public R<PreSellOrderDTO> getByOrderId(@RequestParam("orderId") Long orderId) {
		return R.ok(preSellOrderService.getByOrderId(orderId));
	}

	@PostMapping("/queryByOrderIds")
	public R<List<PreSellOrderDTO>> queryByOrderIds(@RequestBody List<Long> orderIds) {
		return R.ok(preSellOrderService.queryByOrderIds(orderIds));
	}
}
