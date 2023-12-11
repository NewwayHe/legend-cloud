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
 * 系统配置数据类型
 *
 * @author legendshop
 */
public enum SysParamDataType implements StringEnum {

	/**
	 * string
	 */
	STRING("String"),

	/**
	 * boolean
	 */
	BOOLEAN("Boolean"),

	/**
	 * checkbox
	 */
	CHECKBOX("CheckBox"),

	/**
	 * 文件
	 */
	FILE("File");

	private String value;


	SysParamDataType(String value) {
		this.value = value;
	}

	@Override
	public String value() {
		return value;
	}
}
