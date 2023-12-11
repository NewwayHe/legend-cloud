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
 * 拼团、团购活动限购枚举
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum LimitTypeEnum {


	/**
	 * 不限购
	 */
	NOT_LIMIT(0),

	/**
	 * 限制每天每人购买每个SKU
	 */
	DAY_PERSON_SKU(1),

	/**
	 * 限制每天每人购买活动商品
	 */
	DAY_PERSON_PRODUCT(2),

	/**
	 * 限制活动期间每人购买每个SKU
	 */
	ACTIVITY_PERSON_SKU(3),

	/**
	 * 限制活动期间每人购买活动商品
	 */
	ACTIVITY_PERSON_PRODUCT(4),
	;

	private Integer value;

	/**
	 * 是否存在
	 *
	 * @param value
	 * @return
	 */
	public static boolean isExist(Integer value) {
		LimitTypeEnum[] limitTypeEnums = values();
		for (LimitTypeEnum limitTypeEnum : limitTypeEnums) {
			if (limitTypeEnum.value.equals(value)) {
				return true;
			}
		}
		return false;
	}
}
