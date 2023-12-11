/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.enums;

import lombok.Getter;

/**
 * 用户钱包收支业务类型
 *
 * @author legendshop
 */
@Getter
public enum WalletBusinessTypeEnum {

	DISTRIBUTION_REWARDS(10, "分销奖励"),

	SELF_DISTRIBUTION_REWARDS(11, "自购返佣"),

	PAYMENT_RECHARGE(20, "付款充值"),

	PLATFORM_COMPENSATION(30, "平台补偿"),

	ORDER_DEDUCTION(40, "订单抵扣"),

	ORDER_OVERTIME_REFUND(41, "订单抵扣退还"),

	PAYMENT_DEDUCTION(50, "支付抵扣"),

	PAYMENT_DEDUCTION_REFUND(51, "支付抵扣退还"),

	CASH_WITHDRAWAL(60, "现金提现"),

	PLATFORM_DEDUCTION(90, "平台扣款"),

	REFUND_COMPENSATION(100, "订单退款"),

	DISTRIBUTION_WITHDRAW(110, "佣金提现"),

	;

	final Integer code;
	final String desc;

	WalletBusinessTypeEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static String getValue(String name) {
		WalletBusinessTypeEnum[] licenseEnums = values();
		for (WalletBusinessTypeEnum licenseEnum : licenseEnums) {
			if (licenseEnum.name().equals(name)) {
				return licenseEnum.getDesc();
			}
		}
		return null;
	}


	public static WalletBusinessTypeEnum getCode(Integer code) {
		WalletBusinessTypeEnum[] licenseEnums = values();
		for (WalletBusinessTypeEnum licenseEnum : licenseEnums) {
			if (licenseEnum.getCode().equals(code)) {
				return licenseEnum;
			}
		}
		return null;
	}


}
