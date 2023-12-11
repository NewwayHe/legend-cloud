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

import java.math.BigDecimal;

/**
 * 优惠券规则枚举
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum CouponRuleEnum {


	/**
	 * 商品满减
	 */
	FULL_REDUCTION("店铺满减券计算规则"),

	/**
	 * 商品无门槛立减
	 */
	INSTANT_REDUCTION("商品立减券计算规则");

	/**
	 * 获取规则枚举
	 *
	 * @param minPoint
	 * @return
	 */
	public static CouponRuleEnum getRule(BigDecimal minPoint) {
		if (minPoint.compareTo(BigDecimal.ZERO) == 0) {
			return INSTANT_REDUCTION;
			//无门槛券
		}
		return FULL_REDUCTION;
	}

	private final String desc;

}
