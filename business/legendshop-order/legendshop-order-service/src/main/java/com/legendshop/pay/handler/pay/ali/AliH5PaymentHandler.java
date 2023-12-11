/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.pay.ali;

import com.legendshop.pay.dto.AliPaymentDTO;
import org.springframework.stereotype.Component;

/**
 * @author legendshop
 */
@Component("H5_ALI_PAY")
public class AliH5PaymentHandler extends AbstractAliPaymentHandler {


	@Override
	public String postRequest(AliPaymentDTO payment) throws Exception {
		return aliPaySDKFactory.paymentHandlerWap().pay(payment.getPayDescription(), payment.getNumber(), payment.getAmount().toString(), payment.getQuitUrl(), payment.getReturnUrl() + "?source=H5").body;
	}
}
