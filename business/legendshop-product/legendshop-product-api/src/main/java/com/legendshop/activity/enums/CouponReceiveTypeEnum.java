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
 * 优惠券领取方式枚举
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum CouponReceiveTypeEnum {

	/**
	 * 免费领取
	 */
	FREE(0),

	/**
	 * 卡密兑换
	 */
	PWD(1),

	/**
	 * 积分兑换
	 */
	INTEGRAL(2),

	/**
	 * 店铺对指定用户发放
	 */
	TARGET_USER(3);


	private final Integer value;

}
