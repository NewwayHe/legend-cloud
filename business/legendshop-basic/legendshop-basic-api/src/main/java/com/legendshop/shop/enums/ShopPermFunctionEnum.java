/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.enums;

import com.legendshop.common.core.enums.StringEnum;

/**
 * 商家子账号权限类型
 *
 * @author legendshop
 */
public enum ShopPermFunctionEnum implements StringEnum {

	/**
	 * 商品详情
	 */
	CHECK_FUNC("check"),

	/**
	 * 商品列表
	 */
	EDITOR_FUNC("editor"),

	;

	private final String value;

	private ShopPermFunctionEnum(String value) {
		this.value = value;
	}

	@Override
	public String value() {
		return value;
	}
}
