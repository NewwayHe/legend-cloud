/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
@Schema(description = "今日与昨日支付订单BO")
@Builder
public class PaidOrderToDayBO implements Serializable {

	private static final long serialVersionUID = 7421903970225895240L;

	/**
	 * 今日支付订单数量
	 */
	@Schema(description = "今日支付订单数量")
	private Integer todayPaidOrderCount;

	/**
	 * 今日支付金额
	 */
	@Schema(description = "今日支付金额")
	private BigDecimal todayPaidOrderAmount;

	/**
	 * 昨日支付订单数量
	 */
	@Schema(description = "昨日支付订单数量")
	private Integer yesterdayPaidOrderCount;

	/**
	 * 昨日支付金额
	 */
	@Schema(description = "昨日支付金额")
	private BigDecimal yesterdayPaidOrderAmount;

	/**
	 * 今日与昨日支付订单数量比较
	 */
	@Schema(description = "今日与昨日支付订单数量比较")
	private Integer comparePaidOrderCount;

	/**
	 * 今日与昨日支付金额比较
	 */
	@Schema(description = "今日与昨日支付金额比较")
	private BigDecimal comparePaidOrderAmount;
}
