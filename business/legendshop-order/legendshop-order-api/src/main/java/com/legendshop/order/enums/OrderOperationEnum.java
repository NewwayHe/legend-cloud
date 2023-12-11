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
 * 订单操作状态Enum
 *
 * @author legendshop
 */
public enum OrderOperationEnum implements StringEnum {

	/**
	 * 下订单.
	 */
	ORDER_CAPTURE("CA"),

	/**
	 * 删除订单.
	 */
	ORDER_DEL("DE"),

	/**
	 * 改价格.
	 */
	PRICE_CHANGE("PC"),

	/**
	 * 增加积分.
	 */
	CREDIT_SCORE("CS"),

	/**
	 * 使用积分.
	 */
	DEBIT_SCORE("DS"),

	/**
	 * 超时.
	 */
	ORDER_OVER_TIME("OT"),

	/**
	 * 订单状态变化.
	 */
	CHANGE_STATUS("ST");

	/**
	 * The value.
	 */
	private final String value;

	/**
	 * Instantiates a new sub status enum.
	 *
	 * @param value the value
	 */
	private OrderOperationEnum(String value) {
		this.value = value;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see com.legendshop.core.constant.StringEnum#value()
	 */
	@Override
	public String value() {
		return this.value;
	}

	/**
	 * Instance.
	 *
	 * @param name the name
	 * @return true, if successful
	 */
	public static boolean instance(String name) {
		OrderOperationEnum[] licenseEnums = values();
		for (OrderOperationEnum licenseEnum : licenseEnums) {
			if (licenseEnum.name().equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the value.
	 *
	 * @param name the name
	 * @return the value
	 */
	public static String getValue(String name) {
		OrderOperationEnum[] licenseEnums = values();
		for (OrderOperationEnum licenseEnum : licenseEnums) {
			if (licenseEnum.name().equals(name)) {
				return licenseEnum.value();
			}
		}
		return null;
	}
}
