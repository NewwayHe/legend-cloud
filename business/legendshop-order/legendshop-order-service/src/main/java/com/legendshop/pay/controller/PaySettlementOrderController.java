/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.controller;

import com.legendshop.common.core.constant.R;
import com.legendshop.pay.dto.PaySettlementOrderDTO;
import com.legendshop.pay.service.PaySettlementOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/settlement/order")
public class PaySettlementOrderController {

	final PaySettlementOrderService paySettlementOrderService;

	@GetMapping(value = "/queryByOrderNumber")
	public R<List<PaySettlementOrderDTO>> querySnByOrderNumber(@RequestParam(value = "orderNumber") String orderNumber) {
		return R.ok(paySettlementOrderService.querySnByOrderNumber(orderNumber));
	}
}
