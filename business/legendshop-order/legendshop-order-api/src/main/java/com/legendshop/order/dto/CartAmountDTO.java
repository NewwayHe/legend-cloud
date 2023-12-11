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

import java.math.BigDecimal;

/**
 * 购物车金额DTO
 *
 * @author legendshop
 */
@Data
public class CartAmountDTO {

	@Schema(description = "总金额")
	private BigDecimal totalAmount;

	@Schema(description = "优惠金额")
	private Double DiscountAmount;

}
