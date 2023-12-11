/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.enums;

import com.legendshop.common.core.enums.StringEnum;

/**
 * 浏览历史来源
 *
 * @author legendshop
 */
public enum VitLogSourceEnum implements StringEnum {

	/**
	 * PC端
	 */
	PC("pc"),

	/**
	 * 移动端
	 */
	MOBILE("mobile"),

	;

	private final String value;

	VitLogSourceEnum(String value) {
		this.value = value;
	}

	@Override
	public String value() {
		return value;
	}
}
