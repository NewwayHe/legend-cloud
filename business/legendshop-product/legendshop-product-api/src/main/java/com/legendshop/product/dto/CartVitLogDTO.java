/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 加入购物车DTO
 *
 * @author legendshop
 * @create: 2021-07-02 17:09
 */
@Data
public class CartVitLogDTO implements Serializable {

	private static final long serialVersionUID = 2740908707777807008L;

	/**
	 * 店铺id
	 */
	@Schema(description = "店铺id")
	private Long shopId;

	/**
	 * 商品id
	 */
	@Schema(description = "商品id")
	private Long productId;

	/**
	 * 商品名称
	 */
	private String productName;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * skuId
	 */
	@Schema(description = "skuId")
	private Long skuId;

	/**
	 * 购物车产品个数
	 */
	@Schema(description = "数量")
	private Integer count;

	@Schema(description = "来源")
	private String source;
}
