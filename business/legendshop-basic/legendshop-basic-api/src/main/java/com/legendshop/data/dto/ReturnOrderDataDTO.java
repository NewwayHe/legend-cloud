/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
public class ReturnOrderDataDTO {

	/**
	 * 售后订单数量
	 */
	private Integer returnOrderNum;

	/**
	 * 售后订单金额
	 */
	private BigDecimal returnOrderAmount;

}
