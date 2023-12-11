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
import com.legendshop.order.dto.OrderBusinessSumDTO;
import com.legendshop.order.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/orderBusiness", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderBusinessDataController {

	private final OrderService orderService;


	@GetMapping("/getOrderSumByShopId")
	public R<OrderBusinessSumDTO> getOrderSumByShopId(@RequestParam(value = "shopId") Long shopId) {
		return orderService.getOrderSumByShopId(shopId);
	}


}
