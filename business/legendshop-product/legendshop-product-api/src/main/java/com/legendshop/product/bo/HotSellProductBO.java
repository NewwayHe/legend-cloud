/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 热销商品
 *
 * @author legendshop
 */
@Data
public class HotSellProductBO implements Serializable {

	private Long productId;

	/**
	 * 商品名称
	 */
	private String productName;

	/**
	 * 图片
	 */
	private String pic;

	/**
	 * 最小金额
	 */
	private BigDecimal minPrice;

	/**
	 * 最大金额
	 */
	private BigDecimal maxPrice;

	/**
	 * 状态
	 */
	private String status;
}
