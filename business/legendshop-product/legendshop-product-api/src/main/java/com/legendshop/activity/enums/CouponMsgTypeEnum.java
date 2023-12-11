/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.enums;

import com.legendshop.common.core.enums.StringEnum;

/**
 * 礼券通知方式
 *
 * @author legendshop
 */
public enum CouponMsgTypeEnum implements StringEnum {

	/**
	 * 产品引用类型 *.
	 */
	INTERIOR("站内信"),

	/**
	 * 用户引用
	 */
	SMS("短信");

	private String value;

	/**
	 * (non-Javadoc)
	 *
	 * @see com.legendshop.core.constant.IntegerEnum#value()
	 */
	@Override
	public String value() {
		return value;
	}

	/**
	 * Instantiates a new order status enum.
	 *
	 * @param value the value
	 */
	CouponMsgTypeEnum(String value) {
		this.value = value;
	}
}
