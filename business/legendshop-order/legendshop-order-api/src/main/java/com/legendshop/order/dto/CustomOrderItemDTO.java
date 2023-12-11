/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author legendshop
 * @version 1.0.0
 * @title QueryOrderItemResponse
 * @date 2022/5/24 16:24
 * @description：
 */
@Data
public class CustomOrderItemDTO implements Serializable {

	private static final long serialVersionUID = 6482972700477361999L;

	/**
	 * 商品ID
	 */
	private Long productId;

	/**
	 * 图片
	 */
	private String pic;

	/**
	 * 商品名称
	 */
	private String productName;

	/**
	 * 商品价格
	 */
	private BigDecimal price;

	/**
	 * 商品原价
	 */
	private BigDecimal originalPrice;
}
