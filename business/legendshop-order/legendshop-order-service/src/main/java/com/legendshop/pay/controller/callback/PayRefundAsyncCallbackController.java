/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.controller.callback;

import com.legendshop.pay.handler.callback.refund.CallbackRefundHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 支付统一异步回调接口
 *
 * @author legendshop
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/refund/notify", produces = MediaType.APPLICATION_JSON_VALUE)
public class PayRefundAsyncCallbackController {

	final Map<String, CallbackRefundHandler> refundHandler;

	/**
	 * 支付异步回调接口执行代码
	 *
	 * @param request  the request
	 * @param response the response
	 * @return String
	 */
	@RequestMapping("/{payWayCode}")
	public String payAsyncCallback(@PathVariable("payWayCode") String payWayCode, HttpServletRequest request, HttpServletResponse response) {
		CallbackRefundHandler callbackRefundHandler = this.refundHandler.get(payWayCode);
		if (null == callbackRefundHandler) {
			log.error("[ pay-service-payCallback ] 没有符合的回调处理类 payWayCode：{}", payWayCode);
			return null;
		}
		return callbackRefundHandler.handler(request, response);
	}


}
