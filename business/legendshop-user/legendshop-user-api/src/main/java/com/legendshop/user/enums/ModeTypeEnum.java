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
 * 资金流向枚举
 *
 * @author legendshop
 */
public enum ModeTypeEnum implements StringEnum {

	/**
	 * 收入
	 */
	INCOME("INCOME"),

	/**
	 * 支出
	 */
	EXPENDITURE("EXPENDITURE");

	private String value;

	ModeTypeEnum(String value) {
		this.value = value;
	}

	@Override
	public String value() {
		return value;
	}

	public static ModeTypeEnum getInstance(String value) {
		for (ModeTypeEnum moneyStatusEnum : ModeTypeEnum.values()) {
			if (moneyStatusEnum.value.equals(value)) {
				return moneyStatusEnum;
			}
		}
		return null;
	}
}
