/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.enums;

import com.legendshop.common.core.enums.StringEnum;

/**
 * 支付方式
 *
 * @author legendshop
 */
public enum PayTypeEnum implements StringEnum {


	/**
	 * 余额钱包
	 */
	WALLET_PAY("0", "余额钱包"),

	/**
	 * 支付宝
	 */
	ALI_PAY("1", "支付宝"),

	/**
	 * 微信
	 */
	WX_PAY("2", "微信"),

	/**
	 * 模拟支付
	 */
	SIMULATE_PAY("3", "模拟支付"),

	/**
	 * 线下打款
	 */
	OFFLINE("4", "线下打款"),

	/**
	 * 免付
	 */
	FREE_PAY("5", "免付"),

	/**
	 * 易宝微信
	 */
	YEEPAY_WX_PAY("6", "易宝微信"),

	/**
	 * 易宝支付宝
	 */
	YEEPAY_ALI_PAY("7", "易宝支付宝"),

	/**
	 * 易宝银联支付
	 */
	YEEPAY_UNION_PAY("8", "易宝银联支付"),

	;

	private String value;

	private String valueName;

	PayTypeEnum(String value, String valueName) {
		this.value = value;
		this.valueName = valueName;
	}

	@Override
	public String value() {
		return value;
	}

	public String getValueName() {
		return valueName;
	}

	public static String getDesc(String value) {
		for (PayTypeEnum payTypeEnum : PayTypeEnum.values()) {
			if (payTypeEnum.value().equalsIgnoreCase(value)) {
				return payTypeEnum.getValueName();
			}
		}
		return null;
	}

	public static String getName(String value) {
		for (PayTypeEnum payTypeEnum : PayTypeEnum.values()) {
			if (payTypeEnum.name().equalsIgnoreCase(value)) {
				return payTypeEnum.getValueName();
			}
		}
		return null;
	}
}
