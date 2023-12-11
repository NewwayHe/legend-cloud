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
 * 优惠券状态枚举
 * -2:已删除 -1：已失效 0：未开始 1：进行中 2：已暂停 3：已结束
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum CouponStatusEnum {

	/**
	 * 已删除
	 */
	DELETE(-2),

	/**
	 * 已失效
	 */
	OFF_LINE(-1),

	/**
	 * 未开始
	 */
	NOT_STARTED(0),

	/**
	 * 进行中
	 */
	CONTAINS(1),

	/**
	 * 已暂停
	 */
	PAUSE(2),

	/**
	 * 已结束
	 */
	FINISHED(3);

	private final Integer value;
}


