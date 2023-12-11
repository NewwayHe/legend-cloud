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
import com.legendshop.order.dto.OrderRefundReturnDTO;
import com.legendshop.order.service.OrderRefundReturnService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/refund-return", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderRefundReturnController {

	private final OrderRefundReturnService orderRefundReturnService;


	@PostMapping("/getByRefundSn")
	public R<OrderRefundReturnDTO> getByRefundSn(@RequestParam("refundSn") String refundSn) {
		return R.ok(orderRefundReturnService.getByRefundSn(refundSn));
	}

	@PostMapping("/refundHandler")
	public R<Void> refundHandler(@RequestParam("payRefundSn") String payRefundSn, @RequestParam("refundStatus") Boolean refundStatus) {
		return orderRefundReturnService.refundHandler(payRefundSn, refundStatus);
	}

	@PostMapping("/callback")
	public R<Void> refundCallback(@RequestParam(value = "refundSn") String refundSn) {
		return this.orderRefundReturnService.refundCallback(refundSn);
	}
}
