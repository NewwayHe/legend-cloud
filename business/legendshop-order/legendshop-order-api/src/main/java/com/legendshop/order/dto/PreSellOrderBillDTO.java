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

/**
 * 账单结算预售订单
 *
 * @author legendshop
 */
@Data
public class PreSellOrderBillDTO implements Serializable {

	private static final long serialVersionUID = 6666660480973886756L;


	private Long id;

	/**
	 * 店铺ID
	 */
	@Schema(description = "店铺ID")
	private Long shopId;

	/**
	 * 订购ID
	 */
	@Schema(description = "订购ID")
	private Long orderId;

	/**
	 * 定金金额
	 */
	@Schema(description = "定金金额")
	private BigDecimal preDepositPrice;
}
