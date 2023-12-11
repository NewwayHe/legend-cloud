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
 * 促销活动tab类型枚举
 *
 * @author legendshop
 */
public enum PromotionSearchTypeEnum implements StringEnum {

	/**
	 * 未发布
	 */
	NO_PUBLISH("NO_PUBLISH"),

	/**
	 * 未开始
	 */
	NOT_STARTED("NOT_STARTED"),

	/**
	 * 进行中
	 */
	ONLINE("ONLINE"),

	/**
	 * 已暂停
	 */
	PAUSE("PAUSE"),

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
	PromotionSearchTypeEnum(String value) {
		this.value = value;
	}


	@Override
	public String value() {
		return this.value;
	}

	/**
	 * 匹配类型
	 *
	 * @param
	 * @return
	 */
	public static PromotionSearchTypeEnum matchType(String type) {
		for (PromotionSearchTypeEnum typeEnum : PromotionSearchTypeEnum.values()) {
			if (typeEnum.name().equalsIgnoreCase(type)) {
				return typeEnum;
			}
		}
		return null;
	}


}
