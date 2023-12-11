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
 * @author legendshop
 */
@Data
@Schema(description = "店铺商品概况")
public class ProductDataGoodsByShopDTO implements Serializable {

	/**
	 * 商品SPU总数
	 */
	@Schema(description = "商品SPU总数")
	private Integer spuNum;

	/**
	 * 商品SKU总数
	 */
	@Schema(description = "商品SKU总数")
	private Integer skuNum;

	/**
	 * 在售商品SKU总数
	 */
	@Schema(description = "在售商品SKU总数")
	private Integer skuNumSale;

}
