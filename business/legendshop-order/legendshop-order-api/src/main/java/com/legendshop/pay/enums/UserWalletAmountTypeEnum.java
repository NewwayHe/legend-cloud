/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author legendshop
 * @version 1.0.0
 * @title WalletAmountTypeEnum
 * @date 2022/4/27 14:09
 * @description：用户钱包操作金额类型
 */
@Getter
@AllArgsConstructor
public enum UserWalletAmountTypeEnum {

	/**
	 * 冻结金额
	 */
	FROZEN_AMOUNT("FROZEN_AMOUNT", "冻结金额"),

	/**
	 * 可用金额
	 */
	AVAILABLE_AMOUNT("AVAILABLE_AMOUNT", "可用金额");

	final String value;
	final String desc;
}
