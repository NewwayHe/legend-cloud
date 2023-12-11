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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "退款请求参数")
public class RefundRequestDTO implements Serializable {

	private static final long serialVersionUID = -7886407989329384023L;

	@Schema(description = "退款结算单ID")
	private Long id;

	@Schema(description = "支付单号")
	private String number;

	@Schema(description = "退款单号")
	private String refundSn;

	@Schema(description = "退款金额")
	private BigDecimal refundAmount;

	@Schema(description = "订单金额")
	private BigDecimal orderAmount;

}
