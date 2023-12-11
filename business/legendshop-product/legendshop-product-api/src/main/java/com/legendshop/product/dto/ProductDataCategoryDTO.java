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

import java.math.BigDecimal;
import java.util.List;

/**
 * 类目饼状图数据
 *
 * @author legendshop
 */
@Data
@Schema(description = "类目饼状图数据")
public class ProductDataCategoryDTO {

	/**
	 * 类目id
	 */
	@Schema(description = "类目id")
	private Long id;

	/**
	 * 类目名称
	 */
	@Schema(description = "类目名称")
	private String categoryName;

	/**
	 * 类目下SKU数量
	 */
	@Schema(description = "类目下SKU数量")
	private Integer skuNum;

	/**
	 * 类目占比
	 */
	@Schema(description = "类目占比")
	private BigDecimal percentage;

	/**
	 * 子类目数据
	 */
	@Schema(description = "子类目数据")
	private List<ProductDataCategoryDTO> categoryDTOList;


}
