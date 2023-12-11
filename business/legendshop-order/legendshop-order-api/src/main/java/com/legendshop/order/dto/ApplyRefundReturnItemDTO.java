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
 * 售后订单数据
 *
 * @author legendshop
 * @create: 2020-12-23 17:28
 */
@Data
public class ApplyRefundReturnItemDTO implements Serializable {

	private static final long serialVersionUID = 3147244487341757049L;

	@Schema(description = "id")
	private Long id;

	@Schema(description = "图片")
	private String pic;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "最多退款金额")
	private BigDecimal refundAmount;

	public ApplyRefundReturnItemDTO() {
		this.status = 1;
	}
}
