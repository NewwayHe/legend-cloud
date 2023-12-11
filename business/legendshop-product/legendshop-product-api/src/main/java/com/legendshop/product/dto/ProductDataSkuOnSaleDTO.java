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
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 在售商品SKU数量统计
 *
 * @author legendshop
 */
@Data
@Builder
@Schema(description = "在售商品SKU数量统计")
public class ProductDataSkuOnSaleDTO implements Serializable {

	/**
	 * 在售商品SKU总数
	 */
	@Schema(description = "在售商品SKU总数")
	private Integer skuTotalNumOnSale;

	/**
	 * 在售商品SKU日增数
	 */
	@Schema(description = "在售商品SKU日增数")
	private Integer skuTotalNumByDayOnSale;

	/**
	 * 在售商品SKU周增数
	 */
	@Schema(description = "在售商品SKU周增数")
	private Integer skuTotalNumByWeekOnSale;

	/**
	 * 在售商品SKU月增数
	 */
	@Schema(description = "在售商品SKU月增数")
	private Integer skuTotalNumByMonthOnSale;
}
