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
 * 优惠劵不可用原因
 *
 * @author legendshop
 */
public enum CouponUnavailableTypeEnum implements StringEnum {

	/**
	 * 可用
	 */
	AVAILABLE("优惠劵可用"),

	/**
	 * 不可选
	 */
	AFTER_DISCOUNT_MIN_POINT("优惠后金额不满足使用门槛"),

	/**
	 * 不可用，使用门槛不达标
	 */
	MIN_POINT("未达到优惠劵最低使用金额"),

	/**
	 * 不可用，使用时间不达标
	 */
	USE_START_TIME("未到优惠劵开始使用时间"),

	/**
	 * 不可用，所结算商品中没有符合条件商品
	 */
	PRODUCT("所结算商品中没有符合条件商品"),

	/**
	 * 不可用，分销商商品不可使用平台优惠卷
	 */
	DISTRIBUTION("分销商商品不可使用平台优惠卷"),

	;

	/**
	 * The num.
	 */
	private String value;

	CouponUnavailableTypeEnum(String value) {
		this.value = value;
	}

	@Override
	public String value() {
		return value;
	}

}
