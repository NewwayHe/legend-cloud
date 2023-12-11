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
 * 优惠券使用类型枚举
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum CouponUseTypeEnum {


	/**
	 * 指定排除商品，不排除的参加
	 */
	EXCLUDE(-1),
	/**
	 * 全场通用
	 */
	GENERAL(0),

	/**
	 * 指定包含的商品参加
	 */
	INCLUDE(1),

	;

	private final Integer value;

	public static CouponUseTypeEnum fromCode(Integer value) {
		try {
			return values()[value + 1];
		} catch (Exception e) {
			return null;
		}
	}

	public static Boolean isExist(Integer value) {
		CouponUseTypeEnum[] couPonUserTypeEnums = values();
		for (CouponUseTypeEnum couponUseTypeEnum : couPonUserTypeEnums) {
			if (couponUseTypeEnum.getValue().equals(value)) {
				return true;
			}
		}
		return false;
	}
}
