/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.enums;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单类型枚举
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum OrderTypeEnum {

	/**
	 * 普通订单
	 */
	@Schema(description = "普通订单", requiredMode = Schema.RequiredMode.REQUIRED)
	ORDINARY("O", "普通订单"),

	/**
	 * 预售订单
	 */
	@Schema(description = "预售订单", requiredMode = Schema.RequiredMode.REQUIRED)
	PRE_SALE("P", "预售订单"),

	/**
	 * 积分订单
	 */
	@Schema(description = "积分订单", requiredMode = Schema.RequiredMode.REQUIRED)
	INTEGRAL("I", "积分订单"),
	/**
	 * 门店自提订单
	 */
	@Schema(description = "门店自提订单", requiredMode = Schema.RequiredMode.REQUIRED)
	SINCE_MENTION("SM", "门店自提订单");


	private final String value;

	private final String des;


	/**
	 * 是否存在
	 */
	public static boolean instance(String name) {
		OrderTypeEnum[] licenseEnums = values();
		for (OrderTypeEnum licenseEnum : licenseEnums) {
			if (licenseEnum.name().equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取值
	 */
	public static String getValue(String name) {
		OrderTypeEnum[] licenseEnums = values();
		for (OrderTypeEnum orderTypeEnum : licenseEnums) {
			if (orderTypeEnum.value.equals(name)) {
				return orderTypeEnum.getValue();
			}
		}
		return null;
	}

	/**
	 * 获取值
	 */
	public static String getNameByValue(String value) {
		OrderTypeEnum[] licenseEnums = values();
		for (OrderTypeEnum orderTypeEnum : licenseEnums) {
			if (orderTypeEnum.value.equals(value)) {
				return orderTypeEnum.name();
			}
		}
		return null;
	}

	/**
	 * 获取des
	 */
	public static String getDes(String value) {
		for (OrderTypeEnum orderType : OrderTypeEnum.values()) {
			if (orderType.getValue().equalsIgnoreCase(value)) {
				return orderType.getDes();
			}
		}
		return OrderTypeEnum.ORDINARY.getDes();
	}
}
