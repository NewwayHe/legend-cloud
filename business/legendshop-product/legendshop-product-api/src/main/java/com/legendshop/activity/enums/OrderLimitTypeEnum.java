/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.enums;

import lombok.Getter;

/**
 * 团购、拼团活动每单限购枚举
 *
 * @author legendshop
 */
@Getter
public enum OrderLimitTypeEnum {
	/**
	 * 不限购
	 */
	NOT_LIMIT(0),

	/**
	 * 每人每单购买商品
	 */
	EVERY_ORDER(1),
	;

	private Integer value;

	OrderLimitTypeEnum(Integer value) {
		this.value = value;
	}

	/**
	 * 是否存在
	 *
	 * @param value
	 * @return
	 */
	public static boolean isExist(Integer value) {
		OrderLimitTypeEnum[] orderLimitTypeEnums = values();
		for (OrderLimitTypeEnum orderLimitTypeEnum : orderLimitTypeEnums) {
			if (orderLimitTypeEnum.value.equals(value)) {
				return true;
			}
		}
		return false;
	}
}
