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
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 属性来源："USER"; 用户自定义，"SYS"：系统自带
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum ProductPropertySourceEnum implements StringEnum {

	//商家自定义  user
	USER("USER"),

	//系统定义
	SYSTEM("SYS");

	/**
	 * The value.
	 */
	private final String value;

	@Override
	public String value() {
		return value;
	}
}
