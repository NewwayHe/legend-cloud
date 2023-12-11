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
 * 商品SPU数量统计
 *
 * @author legendshop
 */
@Data
@Builder
@Schema(description = "商品SPU数量统计")
public class ProductDataSpuDTO implements Serializable {

	/**
	 * 商品SPU总数
	 */
	@Schema(description = "商品SPU总数")
	private Integer spuTotalNum;

	/**
	 * 商品SPU日增数
	 */
	@Schema(description = "商品SPU日增数")
	private Integer spuTotalNumByDay;

	/**
	 * 商品SKU周增数
	 */
	@Schema(description = "商品SKU周增数")
	private Integer spuTotalNumByWeek;

	/**
	 * 商品SPU月增数
	 */
	@Schema(description = "商品SPU月增数")
	private Integer spuTotalNumByMonth;
}
