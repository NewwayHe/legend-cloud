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

import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
public class RefundItemDTO {

	@Schema(description = "支付单号")
	private String number;

	@Schema(description = "退款单号")
	private String refundSn;

	@Schema(description = "退款类型")
	private String payTypeId;

	@Schema(description = "退款金额")
	private BigDecimal refundAmount;

	@Schema(description = "订单金额")
	private BigDecimal payAmount;

}
