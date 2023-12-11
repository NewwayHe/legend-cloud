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
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 购物车商品项选择促销活动
 *
 * @author legendshop
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartChangePromotionDTO implements Serializable {

	private static final long serialVersionUID = 4735968373998293160L;

	/**
	 * 购物车id
	 */
	@Schema(description = "购物车ID")
	private Long id;


	/**
	 * skuId
	 */
	@NotNull(message = "skuId不能为空")
	@Schema(description = "单品Id", required = true)
	private Long skuId;


	/**
	 * 活动ID
	 */
	@Schema(description = "营销活动id")
	private Long marketingId = 0L;
}
