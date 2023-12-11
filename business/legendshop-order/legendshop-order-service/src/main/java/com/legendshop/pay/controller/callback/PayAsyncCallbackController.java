/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.controller.callback;

import cn.hutool.core.collection.CollUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.pay.dto.PaySettlementItemDTO;
import com.legendshop.pay.handler.callback.pay.CallbackPayHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 支付统一异步回调接口
 *
 * @author legendshop
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/payNotify", produces = MediaType.APPLICATION_JSON_VALUE)
public class PayAsyncCallbackController {


	final Map<String, CallbackPayHandler> payHandler;

	/**
	 * 支付异步回调接口执行代码
	 *
	 * @param request  the request
	 * @param response the response
	 * @return String
	 */
	@RequestMapping("/notify/{payWayCode}")
	public String payAsyncCallback(@PathVariable("payWayCode") String payWayCode, HttpServletRequest request, HttpServletResponse response) {
		CallbackPayHandler callbackPayHandler = this.payHandler.get(payWayCode);

		if (null == callbackPayHandler) {
			log.error("[ pay-service-payCallback ] 没有符合的回调处理类 payWayCode：{}", payWayCode);
			return null;
		}
		long begin = System.currentTimeMillis();
		R<List<PaySettlementItemDTO>> handlerResult = callbackPayHandler.handler(request, response);
		log.info("\n [支付回调 ,callbackPayHandler.handler ], 耗时-> {} ms !", System.currentTimeMillis() - begin);

		if (!handlerResult.getSuccess()) {
			return handlerResult.getMsg();
		}
		List<PaySettlementItemDTO> settlementItemDTOList = handlerResult.getData();
		if (CollUtil.isEmpty(settlementItemDTOList)) {
			return handlerResult.getMsg();
		}

		begin = System.currentTimeMillis();
		String result = callbackPayHandler.callbackBusinessHandle(settlementItemDTOList);
		log.info("\n [支付回调 ,callbackPayHandler.callbackBusinessHandle ], 耗时-> {} ms !", System.currentTimeMillis() - begin);

		return result;
	}

}
