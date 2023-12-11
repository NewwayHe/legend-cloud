/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 发票订单项dto
 *
 * @author legendshop
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemInvoiceDTO implements Serializable {

	private static final long serialVersionUID = -8432545221212546651L;


	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String productName;

	/**
	 * 商品属性
	 */
	@Schema(description = "商品属性")
	private String attribute;

	/**
	 * 商品图片
	 */
	@Schema(description = "商品图片")
	private String pic;


}
