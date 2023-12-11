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
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum MarketingLimitDiscountsReduceRuleEnum {

	/**
	 * 打折
	 */
	DISCOUNT(1, "打折"),

	/**
	 * 固定价格
	 */
	FIXED_PRICE(2, "固定价格"),

	;
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
