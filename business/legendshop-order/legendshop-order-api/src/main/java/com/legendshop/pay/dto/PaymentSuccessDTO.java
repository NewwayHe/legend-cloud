/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
@Schema(description = "付款成功返回")
public class PaymentSuccessDTO implements Serializable {

	@Schema(description = "支付单号")
	private String paySettlementSn;

	@Schema(description = "第三方支付结果")
	private String paymentResult;
}
