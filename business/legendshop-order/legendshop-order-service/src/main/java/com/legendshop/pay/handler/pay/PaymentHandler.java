/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.pay;

import com.legendshop.common.core.constant.R;
import com.legendshop.pay.dto.PaymentDTO;

/**
 * @author legendshop
 */
public interface PaymentHandler {

	/**
	 * 异步地址
	 */
	String notifyUrl = "/pay/payNotify/notify/";

	/**
	 * 同步地址
	 */
	String synchronizeUrl = "/pay/paySynchronizeCallback";

	/**
	 * 支付结束地址
	 */
	String paymentEnd = "/pay/payEndCallback";

	/**
	 * h5支付成功地址
	 */
	String h5PaySuccessUrl = "/ModuleOrder/submitOrder/orderPayResult?subSettlementSn=";

	/**
	 * 用户PC支付成功地址
	 */
	String PcPaySuccessUrl = "/cart/result?settlementSn=";


	R<String> payment(PaymentDTO payment);

	String getNotifyUrl();
}
