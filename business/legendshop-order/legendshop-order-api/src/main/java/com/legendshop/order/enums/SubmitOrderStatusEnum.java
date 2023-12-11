/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.enums;


import com.legendshop.common.core.enums.StringEnum;

/**
 * @author legendshop
 */
public enum SubmitOrderStatusEnum implements StringEnum {

	/**
	 * 失败
	 */
	ERR("ERR"),

	/**
	 * 没有登录
	 */
	NOT_LOGIN("NOT_LOGIN"),

	/**
	 * 没有购物清单
	 */
	NOT_PRODUCTS("NOT_PRODUCTS"),

	/**
	 * 重复提交订单
	 */
	INVALID_TOKEN("INVALID_TOKEN"),

	/**
	 * token为空
	 */
	NULL_TOKEN("NULL_TOKEN"),

	/**
	 * 商品超出购买限制
	 */
	PROD_RESTRICTION("RESTRICTION"),

	/**
	 * 没有用户地址
	 */
	NO_ADDRESS("NO_ADDRESS"),

	/**
	 * 库存不足
	 */
	UNDERSTOCK("UNDERSTOCK"),

	/**
	 * 无权限购买
	 */
	NOPERMISSION("NOPERMISSION"),

	/**
	 * 参数有误
	 */
	PARAM_ERR("PARAM_ERR");

	/**
	 * The value.
	 */
	private final String value;

	private SubmitOrderStatusEnum(String value) {
		this.value = value;
	}

	@Override
	public String value() {
		return this.value;
	}

}
