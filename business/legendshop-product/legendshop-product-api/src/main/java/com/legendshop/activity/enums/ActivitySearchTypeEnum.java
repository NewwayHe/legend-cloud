/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.enums;


import com.legendshop.common.core.enums.StringEnum;

/**
 * 营销活动 tab选择类型枚举
 *
 * @author legendshop
 */
public enum ActivitySearchTypeEnum implements StringEnum {
	/**
	 * 所有
	 */
	ALL("ALL"),

	/**
	 * 待审核
	 */
	WAIT_AUDIT("WAIT_AUDIT"),

	/**
	 * 未通过
	 */
	NOT_PASS("NOT_PASS"),

	/**
	 * 未开始
	 */
	NOT_STARTED("NOT_STARTED"),

	/**
	 * 进行中
	 */
	ONLINE("ONLINE"),

	/**
	 * 已结束
	 */
	FINISHED("FINISHED"),

	/**
	 * 已失效
	 */
	EXPIRED("EXPIRED"),

	;
	/**
	 * The value.
	 */
	private final String value;

	/**
	 * Instantiates a new visit type enum.
	 *
	 * @param value the value
	 */
	private ActivitySearchTypeEnum(String value) {
		this.value = value;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see com.legendshop.core.constant.StringEnum#value()
	 */
	@Override
	public String value() {
		return this.value;
	}

	/**
	 * 匹配类型
	 *
	 * @param type
	 * @return
	 */
	public static ActivitySearchTypeEnum matchType(String type) {
		for (ActivitySearchTypeEnum typeEnum : ActivitySearchTypeEnum.values()) {
			if (typeEnum.name().equalsIgnoreCase(type)) {
				return typeEnum;
			}
		}
		return null;
	}


}
