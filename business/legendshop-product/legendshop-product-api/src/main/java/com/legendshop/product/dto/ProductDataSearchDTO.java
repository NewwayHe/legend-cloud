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
 * 商品统计搜索概况DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "商品统计搜索概况DTO")
public class ProductDataSearchDTO {

	/**
	 * 关键词
	 */
	@Schema(description = "关键词")
	private String word;

	/**
	 * 词频
	 */
	@Schema(description = "词频")
	private Integer frequency;

}
