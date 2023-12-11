/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.expetion.enums;


import com.legendshop.common.core.expetion.BusinessExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 103=服务端返回/商品模块
 * 01：购物车
 * 02：商品
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum ProductResponseEnum implements BusinessExceptionAssert {

	/**
	 * 购物车异常
	 */
	BASKET_EXCEPTION(10301001, "购物车信息错误！请重新下单"),

	/**
	 * 商品状态异常
	 */
	PRODUCT_STATUS_EXCEPTION(10302001, "商品状态异常"),

	/**
	 *
	 */
	PRODUCT_STOCK_INSUFFICIENT_EXCEPTION(10302002, "商品库存不足异常"),
	;

	/**
	 * 返回码
	 */
	private final int code;
	/**
	 * 返回消息
	 */
	private final String msg;
}
