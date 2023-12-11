/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author legendshop
 * @version 1.0.0
 * @title WalletTransactionTypeEnum
 * @date 2022/4/8 14:37
 * @description： 钱包交易类型
 */
@Getter
@AllArgsConstructor
public enum WalletTransactionTypeEnum {

	/**
	 * 佣金结算
	 */
	COMMISSION_SETTLEMENT("COMMISSION_SETTLEMENT", "佣金结算"),

	/**
	 * 奖励结算
	 */
	REWARD_SETTLEMENT("REWARD_SETTLEMENT", "奖励结算"),

	/**
	 * 佣金提现
	 */
	COMMISSION_WITHDRAWAL("COMMISSION_WITHDRAWAL", "佣金提现"),

	;

	private final String value;

	private final String desc;

	public static String fromDesc(String name) {
		if (StrUtil.isBlank(name)) {
			return "";
		}

		for (WalletTransactionTypeEnum value : WalletTransactionTypeEnum.values()) {
			if (value.name().equals(name)) {
				return value.getDesc();
			}
		}

		return "";
	}
}
