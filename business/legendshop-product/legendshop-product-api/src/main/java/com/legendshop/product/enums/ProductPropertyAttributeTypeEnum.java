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
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum ProductPropertyAttributeTypeEnum implements StringEnum {

	//参数属性  Paramter
	PARAMETER("P"),

	//规格属性 Specification
	SPECIFICATION("S");

	/**
	 * The value.
	 */
	private final String value;

	@Override
	public String value() {
		return value;
	}

	public static Boolean isExist(String value) {
		ProductPropertyAttributeTypeEnum[] values = values();
		for (ProductPropertyAttributeTypeEnum type : values) {
			if (type.getValue().equals(value)) {
				return true;
			}
		}
		return false;
	}
}
