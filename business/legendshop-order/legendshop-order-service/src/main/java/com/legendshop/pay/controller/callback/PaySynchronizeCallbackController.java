/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.controller.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.basic.properties.CommonProperties;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.pay.handler.pay.PaymentHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * 支付同步回调，不进行业务处理
 *
 * @author legendshop
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class PaySynchronizeCallbackController {

	final CommonProperties commonProperties;

	@GetMapping(value = "/paySynchronizeCallback")
	public void paySynchronizeCallback(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String[] outTradeNo = new String[0];
		String[] source = new String[0];
		try {
			Map<String, String[]> parameterMap = request.getParameterMap();
			ObjectMapper objectMapper = new ObjectMapper();
			log.info(" [ Payment Synchronize ] 同步回调参数打印 parameterMap: {}", objectMapper.writeValueAsString(parameterMap));
			outTradeNo = parameterMap.get("out_trade_no");
			source = parameterMap.get("source");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (VisitSourceEnum.PC.name().equals(source[0])) {
			String pcUrl = this.commonProperties.getUserPcDomainName() + PaymentHandler.PcPaySuccessUrl + outTradeNo[0];
			log.info(" [ Payment Synchronize ] 正在跳转用户PC端, {}", pcUrl);
			response.sendRedirect(pcUrl);
		} else {
			String h5Url = this.commonProperties.getUserMobileDomainName() + PaymentHandler.h5PaySuccessUrl + outTradeNo[0];
			log.info(" [ Payment Synchronize ] 正在跳转用户移动端, {}", h5Url);
			response.sendRedirect(h5Url);
		}
	}

}
