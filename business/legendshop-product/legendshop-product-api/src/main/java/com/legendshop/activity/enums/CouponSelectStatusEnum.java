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
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum CouponSelectStatusEnum {

	/**
	 * 不可用
	 */
	UN_AVAILABLE(-2),

	/**
	 * 不可选
	 */
	UN_OPTIONAL(-1),

	/**
	 * 可选
	 */
	OPTIONAL(0),

	/**
	 * 已选中
	 */
	SELECTED(1);

	private final Integer status;


}
