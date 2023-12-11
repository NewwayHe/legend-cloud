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
import com.legendshop.order.dto.OrderRefundReturnDTO;
import com.legendshop.order.mq.producer.OrderProducerService;
import com.legendshop.order.service.OrderRefundReturnService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@RestController
@AllArgsConstructor
public class OrderRefundReturnApiImpl implements OrderRefundReturnApi {

	private final OrderRefundReturnService orderRefundReturnService;
	private final OrderProducerService orderProducerService;

	@Override
	public R<OrderRefundReturnDTO> getByRefundSn(@RequestParam("refundSn") String refundSn) {
		return R.ok(orderRefundReturnService.getByRefundSn(refundSn));
	}

	@Override
	public R<Void> refundHandler(@RequestParam("payRefundSn") String payRefundSn, @RequestParam("refundStatus") Boolean refundStatus) {
		return orderRefundReturnService.refundHandler(payRefundSn, refundStatus);
	}

	@Override
	public R<Void> refundCallback(@RequestParam(value = "refundSn") String refundSn) {
		return this.orderRefundReturnService.refundCallback(refundSn);
	}
}
