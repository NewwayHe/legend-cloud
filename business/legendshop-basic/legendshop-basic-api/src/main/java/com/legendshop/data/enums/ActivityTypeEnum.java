/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.enums;

import lombok.Getter;

/**
 * @author legendshop
 */
@Getter
public enum ActivityTypeEnum {

	/**
	 * 限时折扣
	 */
	LIMIT_DISCOUNT("限时折扣"),

	/**
	 * 满减
	 */
	SUB_MARKETING("满减"),

	/**
	 * 满折
	 */
	DIS_MARKETING("满折"),

	/**
	 * 店铺优惠券
	 */
	SHOP_COUPON("店铺优惠券"),

	/**
	 * 平台优惠券
	 */
	PLARTFORM_COUPON("平台优惠券");


	private String desc;

	ActivityTypeEnum(String desc) {
		this.desc = desc;
	}
}
