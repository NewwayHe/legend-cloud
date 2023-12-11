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
 * 活动促销类型枚举
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum MarketingDesignatedTypeEnum {

	/**
	 * 满减满折
	 */
	REWARD(0, "满减满折"),

	/**
	 * 限时折扣
	 */
	LIMIT_DISCOUNTS(1, "限时折扣");

	private Integer value;

	private String desc;

	public static MarketingDesignatedTypeEnum fromCode(Integer value) {
		if (null != value) {
			for (MarketingDesignatedTypeEnum marketingDesignatedTypeEnum : MarketingDesignatedTypeEnum.values()) {
				if (marketingDesignatedTypeEnum.getValue().equals(value)) {
					return marketingDesignatedTypeEnum;
				}
			}
		}
		return null;
	}
}
