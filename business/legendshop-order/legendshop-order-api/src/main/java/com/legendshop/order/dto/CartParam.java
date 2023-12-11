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
 * 购物车DTO
 *
 * @author legendshop
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartParam implements Serializable {

	private static final long serialVersionUID = 920053626291416366L;

	/**
	 * 购物车id
	 */
	@Schema(description = "购物车ID")
	private Long id;

	/**
	 * 店铺id
	 */
	@NotNull(message = "店铺id不能为空")
	@Schema(description = "店铺id", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long shopId;

	/**
	 * 商品id
	 */
	@NotNull(message = "商品id不能为空")
	@Schema(description = "商品id", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long productId;

	/**
	 * skuId
	 */
	@NotNull(message = "skuId不能为空")
	@Schema(description = "skuId", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long skuId;

	/**
	 * 购物车产品个数
	 */
	@NotNull(message = "数量不能为空")
	@Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
	private Integer count;


	/**
	 * 选中状态 1:选中 0：未选中
	 */
	private Boolean selectFlag = Boolean.TRUE;


	/**
	 * 活动ID
	 */
	@Schema(description = "营销活动id")
	private Long marketingId = 0L;

	@Schema(description = "物料URL")
	private String materialUrl;
}
