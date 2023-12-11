/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author legendshop
 */
@Data
@Schema(description = "下单成功返回参数")
public class SubmitOrderSuccessDTO implements Serializable {


	@Schema(description = "订单号集合")
	private List<String> orderNumberList;

	@Schema(description = "支付单据类型[订单支付:ORDINARY_ORDER,预付款充值:USER_RECHARGE]", allowableValues = "ORDINARY_ORDER,USER_RECHARGE")
	private String settlementType;

	@Schema(description = "订单支付金额")
	private BigDecimal amount;

	/**
	 * 默认为空，只有免付订单才会返回
	 */
	@Schema(description = "支付编码")
	private String paySettlementSn;
}
