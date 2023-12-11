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
 * 优惠券指定用户枚举
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum CouponDesignateEnum {

	/*不指定*/
	NONE(0),

	/*手机号码（输入手机号，用‘，’号隔开，支持批量导入）*/
	MOBILE(1),

	/*平台新注册用户（发布优惠券后才注册的用户）*/
	NEW_REGISTRATION_OF_PLATFORM(2),

	/*平台旧注册用户（发布活动前已注册的用户）*/
	OLD_REGISTRATION_OF_PLATFORM(3),

	/*平台新用户（在平台内没有购买过商品的用户（包括申请售后成功的用户））*/
	PLATFORM_NEW_USERS(4),

	/*平台老用户（在平台内已购买过商品的用户（剔除申请售后的用户））*/
	PLATFORM_OLD_USERS(5),

	/*店铺新用户（在店铺内没有购买过商品的用户（包括申请售后成功的用户））*/
	SHOP_NEW_USER(6),

	/*店铺老用户（买过商品的用户（剔除申请售后的用户））*/
	SHOP_OLD_USER(7);

	private final Integer value;
}
