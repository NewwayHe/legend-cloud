/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.enums;

import com.legendshop.common.core.enums.StringEnum;

/**
 * 商家结算周期.
 *
 * @author legendshop
 */
public enum BillPeriodEnum implements StringEnum {

	/**
	 * 月结.
	 */
	MONTH("MONTH"),

	/**
	 * 按周结算.
	 */
	WEEK("WEEK"),

	/**
	 * 每天一结算
	 */
	DAY("DAY");

	private final String value;

	private BillPeriodEnum(String value) {
		this.value = value;
	}

	@Override
	public String value() {
		return this.value;
	}

	/**
	 * 根据字符串获取对应的实例
	 *
	 * @return
	 */
	public static BillPeriodEnum instance(String value) {
		BillPeriodEnum[] enums = values();
		for (BillPeriodEnum enum1 : enums) {
			if (enum1.value().equals(value)) {
				return enum1;
			}
		}
		return null;
	}
}
