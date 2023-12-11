/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 店铺类型枚举
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum ShopTypeEnum {

	/**
	 * 专营店
	 */
	FRANCHISE(0),

	/**
	 * 旗舰店
	 */
	FLAGSHIP(1),

	/**
	 * 自营
	 */
	SELF_EMPLOYED(2);


	private Integer value;

	public static boolean existValue(Integer value) {

		for (ShopTypeEnum shopTypeEnum : ShopTypeEnum.values()) {
			if (shopTypeEnum.value.equals(value)) {
				return true;
			}
		}
		return false;
	}
}
