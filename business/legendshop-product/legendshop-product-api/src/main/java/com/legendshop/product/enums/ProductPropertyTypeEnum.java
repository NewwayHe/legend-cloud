/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.enums;

import com.legendshop.common.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 属性类型：TXT：文本类型; PIC:图片类型;
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum ProductPropertyTypeEnum implements BaseEnum<String> {
	//文本类型
	TXT("TXT"),
	//图片类型;
	PICTURE("PIC");

	/**
	 * The value.
	 */
	private final String value;

	@Override
	public boolean contains(String value) {
		for (ProductPropertyTypeEnum typeEnum : ProductPropertyTypeEnum.values()) {
			if (typeEnum.name().equals(value)) {
				return true;
			}
		}
		return false;
	}
}
