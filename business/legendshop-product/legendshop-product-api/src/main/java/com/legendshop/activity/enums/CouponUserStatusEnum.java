/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户优惠券状态枚举
 * 优惠券使用状态 -1:已失效；0为未开始；1:未使用 2：已使用
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum CouponUserStatusEnum {

	/**
	 * 已失效
	 */
	INVALID(-1),

	/**
	 * 未开始
	 */
	NOT_STARTED(0),

	/**
	 * 未使用
	 */
	UNUSED(1),

	/**
	 * 已使用
	 */
	USED(2);

	private Integer value;
}
