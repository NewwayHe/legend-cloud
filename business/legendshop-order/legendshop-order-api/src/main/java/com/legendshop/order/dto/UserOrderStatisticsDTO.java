/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户订单统计
 *
 * @author legendshop
 */
@Data
public class UserOrderStatisticsDTO implements Serializable {
	private static final long serialVersionUID = -2207092100837932488L;

	/**
	 * 订单总数
	 */
	private Long count;

	/**
	 * 订单总金额
	 */
	private BigDecimal totalAmount;

	/**
	 * 退款完成订单数
	 */
	private Long refundCount;

	/**
	 * 退款总金额
	 */
	private BigDecimal refundAmount;

	public UserOrderStatisticsDTO() {
		this.count = 0L;
		this.refundCount = 0L;
		this.totalAmount = BigDecimal.ZERO;
		this.refundAmount = BigDecimal.ZERO;
	}
}
