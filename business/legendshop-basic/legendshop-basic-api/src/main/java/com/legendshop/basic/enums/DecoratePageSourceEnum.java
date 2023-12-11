/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;


import com.legendshop.common.core.enums.StringEnum;

/**
 * 装修页面来源
 *
 * @author legendshop
 */
public enum DecoratePageSourceEnum implements StringEnum {

	/**
	 * pc端
	 */
	PC("pc"),

	/**
	 * mobile端
	 */
	MOBILE("mobile"),

	;

	/**
	 * The value.
	 */
	private final String value;

	/**
	 * @param value
	 */
	DecoratePageSourceEnum(String value) {
		this.value = value;
	}


	@Override
	public String value() {
		return this.value;
	}

}
