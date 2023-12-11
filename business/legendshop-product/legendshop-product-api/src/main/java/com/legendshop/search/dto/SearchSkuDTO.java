/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
public class SearchSkuDTO {

	private Long id;

	private Long productId;

	private String image;

	/**
	 * 原价
	 */
	private BigDecimal originalPrice;

	/**
	 * 销售价格
	 */
	private BigDecimal price;


	/**
	 * 限时营销价格
	 */
	private BigDecimal limitDiscountsMarketingPrice = BigDecimal.ZERO;


}
