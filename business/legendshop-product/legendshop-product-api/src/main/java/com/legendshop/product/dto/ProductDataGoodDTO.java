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

/**
 * @author legendshop
 */
@Data
@Schema(description = "商品列表")
public class ProductDataGoodDTO {

	/**
	 * 商品id
	 */
	@Schema(description = "商品id")
	private Integer goodId;

	/**
	 * 商品名
	 */
	@Schema(description = "商品名")
	private String goodName;
}
