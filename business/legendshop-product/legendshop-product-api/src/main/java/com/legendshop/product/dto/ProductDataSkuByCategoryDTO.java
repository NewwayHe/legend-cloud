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
 * 类目状况(类目下所有sku总数排行前五)
 *
 * @author legendshop
 */
@Data
@Schema(description = "类目状况(类目下所有sku总数排行前五)")
public class ProductDataSkuByCategoryDTO {

	/**
	 * 序号
	 */
	@Schema(description = "序号")
	private Integer orderNum;

	/**
	 * 类目名称
	 */
	@Schema(description = "类目名称")
	private String categoryName;

	/**
	 * 商品SKU数量
	 */
	@Schema(description = "商品SKU数量")
	private Integer skuNum;

}
