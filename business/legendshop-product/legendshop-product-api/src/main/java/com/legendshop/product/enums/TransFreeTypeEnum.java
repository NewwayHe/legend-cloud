/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.enums;

import com.legendshop.common.core.enums.IntegerEnum;

/**
 * 条件包邮类型
 *
 * @author legendshop
 */
public enum TransFreeTypeEnum implements IntegerEnum {

	/**
	 * 满件包邮
	 */
	FULL_NUM(1),

	/**
	 * 满金额包邮
	 */
	FULL_MONEY(2);

	private Integer value;

	TransFreeTypeEnum(Integer value) {
		this.value = value;
	}

	@Override
	public Integer value() {
		return value;
	}

	public static TransFreeTypeEnum fromCode(Integer value) {
		if (null != value) {
			for (TransFreeTypeEnum typeEnum : TransFreeTypeEnum.values()) {
				if (typeEnum.value.equals(value)) {
					return typeEnum;
				}
			}
		}
		return null;
	}
}
