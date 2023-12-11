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
 * 订单营业数据dto
 *
 * @author legendshop
 */
@Data
public class OrderBusinessSumDTO implements Serializable {

	private static final long serialVersionUID = 5590178636572617795L;

	/**
	 * 累计消费金额
	 */
	private BigDecimal salesAmount;

	/**
	 * 累计消费订单数量
	 */
	private Integer salesOrderCount;

	/**
	 * 累计退款金额
	 */
	private BigDecimal refundAmount;
}
