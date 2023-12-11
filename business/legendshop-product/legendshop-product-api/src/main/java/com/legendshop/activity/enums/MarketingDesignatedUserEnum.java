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
 * 满减满折、限时折扣活动定向人群枚举
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum MarketingDesignatedUserEnum {

	/**
	 * 平台新注册用户【发布活动后才注册的用户】
	 */
	NEW_REGISTRATION_OF_PLATFORM(1, "平台新注册用户"),

	/**
	 * 平台旧注册用户【发布活动前已注册的用户】
	 */
	OLD_REGISTRATION_OF_PLATFORM(2, "平台旧注册用户"),

	/**
	 * 平台新用户【在平台内没有购买过商品的用户（包括申请售后成功的用户）】
	 */
	PLATFORM_NEW_USERS(3, "平台新用户"),

	/**
	 * 平台老用户【在平台内已购买过商品的用户（剔除申请售后的用户）】
	 */
	PLATFORM_OLD_USERS(4, "平台老用户"),

	/**
	 * 店铺新用户【在店铺内没有购买过商品的用户（包括申请售后成功的用户）】
	 */
	SHOP_NEW_USER(5, "店铺新用户"),

	/**
	 * 店铺老用户【在店铺内已购买过商品的用户（剔除申请售后的用户）】
	 */
	SHOP_OLD_USER(6, "店铺老用户");

	private final Integer value;

	private final String desc;

	public static MarketingDesignatedUserEnum fromCode(Integer value) {
		if (null != value) {
			for (MarketingDesignatedUserEnum marketingDesignatedUserEnum : MarketingDesignatedUserEnum.values()) {
				if (marketingDesignatedUserEnum.getValue().equals(value)) {
					return marketingDesignatedUserEnum;
				}
			}
		}
		return null;
	}
}
