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

/**
 * @author legendshop
 */
public enum FreightModeEnum implements StringEnum {

	/**
	 * Mail transport mode.
	 */
	TRANSPORT_MAIL("mail"),

	/**
	 * Express transport mode.
	 */
	TRANSPORT_EXPRESS("express"),

	/**
	 * EMS transport mode.
	 */
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
	private FreightModeEnum(String value) {
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
}
