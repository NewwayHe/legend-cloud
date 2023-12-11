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
 * 营销类型枚举
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum MarketingTypeEnum {

	CASH(0, "满减"),

	DISCOUNT(1, "满折"),

	LIMITED_TIME(2, "限时折扣"),

	SHOP_COUPON(6, "店铺优惠卷"),

	PLAT_COUPON(7, "平台优惠卷"),

	CASH_DISCOUNT(8, "满减满折");

	private final Integer type;

	private final String desc;

	public static MarketingTypeEnum fromCode(Integer code) {
		try {
			return values()[code];
		} catch (Exception e) {
			return null;
		}
	}
}
