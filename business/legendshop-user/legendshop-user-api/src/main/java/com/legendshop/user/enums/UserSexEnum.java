/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.enums;

import com.legendshop.common.core.enums.StringEnum;

/**
 * 性别
 *
 * @author legendshop
 */
public enum UserSexEnum implements StringEnum {
	/**
	 * 男
	 */
	MALE("M"),
	/**
	 * 女
	 */
	FEMALE("F"),
	/**
	 * 保密
	 */
	SECRET("S");

	private String value;

	UserSexEnum(String value) {
		this.value = value;
	}

	@Override
	public String value() {
		return value;
	}
}
