/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.controller.callback;

import com.legendshop.basic.properties.CommonProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 订单支付中断返回地址
 * * @author legendshop
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class PayEndCallbackController {

	final CommonProperties commonProperties;

	@GetMapping(value = "/payEndCallback")
	public void paySynchronizeCallback(HttpServletResponse response) throws IOException {
		response.sendRedirect(commonProperties.getUserMobileDomainName() + "/ModuleOrder/submitOrder/orderPayResult");
	}

}
