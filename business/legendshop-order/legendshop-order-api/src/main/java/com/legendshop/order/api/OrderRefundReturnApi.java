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
import com.legendshop.order.dto.OrderRefundReturnDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author legendshop
 */
@FeignClient(contextId = "orderRefundReturnApi", value = ServiceNameConstants.ORDER_SERVICE)
public interface OrderRefundReturnApi {

	String PREFIX = ServiceNameConstants.ORDER_SERVICE_RPC_PREFIX;

	/**
	 * 根据退款单号获取退款信息
	 *
	 * @param refundSn
	 * @return
	 */
	@PostMapping(PREFIX + "/refund-return/getByRefundSn")
	R<OrderRefundReturnDTO> getByRefundSn(@RequestParam("refundSn") String refundSn);

	/**
	 * 退款单处理
	 *
	 * @param payRefundSn  支付退款订单号
	 * @param refundStatus 退款状态    true：成功   false：失败
	 * @return
	 */
	@PostMapping(PREFIX + "/refund-return/refundHandler")
	R<Void> refundHandler(@RequestParam("payRefundSn") String payRefundSn, @RequestParam("refundStatus") Boolean refundStatus);

	/**
	 * 退款结果回调处理
	 */
	@PostMapping(value = PREFIX + "/refund/callback")
	R<Void> refundCallback(@RequestParam(value = "refundSn") String refundSn);
}
