/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 拼团活动选择商品
 *
 * @author legendshop
 */
@Data
public class PreSellSkuDTO implements Serializable {

	private static final long serialVersionUID = -8414979356767710211L;

	/**
	 * 产品ID
	 */
	private Long productId;

	/**
	 * 产品名称
	 */
	private String productName;

	/**
	 * 商品价格
	 */
	private Double price;

	/**
	 * 商品现价
	 */
	private Double cash;

	/**
	 * 产品图片
	 */
	private String prodPic;

	/**
	 * 商品库存
	 */
	private Long stocks;

	/**
	 * 实际库存
	 */
	private Long actualStocks;

	/**
	 * skuId
	 */
	private Long skuId;

	/**
	 * sku属性
	 */
	private String cnProperties;

	/**
	 * 商品sku价格
	 */
	private Double skuPrice;

	/**
	 * 商品sku库存
	 */
	private Long skuStocks;

	/**
	 * sku实际库存
	 */
	private Long skuActualStocks;

	/**
	 * Sku名称
	 */
	private String skuName;

	/**
	 * 商家编码
	 */
	private String partyCode;

	/**
	 * SKU图片
	 */
	private String skuPic;

	/**
	 * 预售价，用于查看预售活动详情
	 */
	private Double prSellPrice;

}
