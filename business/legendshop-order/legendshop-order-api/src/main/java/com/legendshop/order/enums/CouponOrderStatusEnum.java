/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.enums;

import com.legendshop.common.core.enums.IntegerEnum;

/**
 * 优惠券状态
 *
 * @author legendshop
 */
public enum CouponOrderStatusEnum implements IntegerEnum {

	USE(1),
	USEOUT(10);


	/**
	 * The num.
	 */
	private Integer num;

	@Override
	public Integer value() {
		return num;
	}

	/**
	 * Instantiates a new order status enum.
	 *
	 * @param num the num
	 */
	CouponOrderStatusEnum(Integer num) {
		this.num = num;
	}
}
