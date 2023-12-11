/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 店铺商品搜索
 *
 * @author legendshop
 * @create: 2021/10/10 9:34
 */
@Data
public class SearchShopProductDTO {

	@Schema(description = "商品ID")
	private Long productId;

	/**
	 * 图片
	 */
	@Schema(description = "图片")
	private String pic;

	/**
	 * 价格
	 */
	@Schema(description = "价格")
	private String price;

	/**
	 * 最小金额
	 */
	@Schema(description = "最小金额")
	private BigDecimal minPrice;

	/**
	 * 最大金额
	 */
	@Schema(description = "最大金额")
	private BigDecimal maxPrice;
}
