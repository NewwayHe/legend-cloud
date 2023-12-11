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
 * 商品SPU访问数量统计
 *
 * @author legendshop
 */
@Data
@Builder
@Schema(description = "商品SPU访问数量统计")
public class ProductDataSpuClickDTO implements Serializable {

	/**
	 * 商品SPU访问总数
	 */
	@Schema(description = "商品SPU访问总数")
	private Integer spuClickTotalNum;

	/**
	 * 商品SPU访问日增数
	 */
	@Schema(description = "商品SPU访问日增数")
	private Integer spuClickNumByDay;

	/**
	 * 商品SPU访问周增数
	 */
	@Schema(description = "商品SPU访问周增数")
	private Integer spuClickNumByWeek;

	/**
	 * 商品SPU访问月增数
	 */
	@Schema(description = "商品SPU访问月增数")
	private Integer spuClickNumByMonth;
}
