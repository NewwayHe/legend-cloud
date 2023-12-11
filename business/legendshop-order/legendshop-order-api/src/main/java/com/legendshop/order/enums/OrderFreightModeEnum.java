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
 * @author legendshop
 */
public enum OrderFreightModeEnum implements StringEnum {

	TRANSPORT_MAIL("mail"),

	TRANSPORT_EXPRESS("express"),

	TRANSPORT_EMS("ems");

	/**
	 * The value.
	 */
	private final String value;

	/**
	 * Instantiates a new visit type enum.
	 *
	 * @param value the value
	 */
	private OrderFreightModeEnum(String value) {
		this.value = value;
	}


	@Override
	public String value() {
		return this.value;
	}
}
