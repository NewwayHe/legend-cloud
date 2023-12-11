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
 * 营销活动类型
 *
 * @author legendshop
 */
@Getter
public enum ActivityEsTypeEnum {

	/**
	 * 积分
	 */
	INTEGRAL(6, "积分"),


	/**
	 * 优惠券
	 */
	COUPON(8, "优惠券"),

	;

	private Integer value;
	private String desc;

	ActivityEsTypeEnum(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public static boolean isExist(Integer value) {
		ActivityEsTypeEnum[] activityEsTypeEnums = values();
		for (ActivityEsTypeEnum activityEsTypeEnum : activityEsTypeEnums) {
			if (activityEsTypeEnum.value.equals(value)) {
				return true;
			}
		}
		return false;
	}

	public static Long createId(Long activityId, ActivityEsTypeEnum activityEsTypeEnum) {
		if (activityId == null || activityEsTypeEnum == null) {
			return null;
		}
		return Long.valueOf(activityEsTypeEnum.value + activityId.toString());
	}

	public static Long createId(Long activityId, Integer value) {
		if (activityId == null || value == null) {
			return null;
		}
		return Long.valueOf(value + activityId.toString());
	}

	public static String fromName(Integer value) {
		if (value == null) {
			return "";
		}

		for (ActivityEsTypeEnum esTypeEnum : ActivityEsTypeEnum.values()) {
			if (esTypeEnum.getValue().equals(value)) {
				return esTypeEnum.name();
			}
		}

		return "";
	}

	public static String fromDesc(Integer value) {
		if (value == null) {
			return "";
		}

		for (ActivityEsTypeEnum esTypeEnum : ActivityEsTypeEnum.values()) {
			if (esTypeEnum.getValue().equals(value)) {
				return esTypeEnum.getDesc();
			}
		}

		return "";
	}
}
