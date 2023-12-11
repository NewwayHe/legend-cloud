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
 * @author legendshop
 */
@Getter
public enum WithdrawTypeEnum {
	WITHDRAW_DISTRIBUTION_WALLET("WITHDRAW_DISTRIBUTION_WALLET", "佣金提现到钱包"),
	ALI_DISTRIBUTION_WALLET("ALI_DISTRIBUTION_WALLET", "佣金提现到支付宝"),
	WECHAT_DISTRIBUTION_WALLET("WECHAT_DISTRIBUTION_WALLET", "佣金提现到微信"),
	ALI_WITHDRAW_WALLET("ALI_WITHDRAW_WALLET", "钱包提现到支付宝"),
	WECHAT_WITHDRAW_WALLET("WECHAT_WITHDRAW_WALLET", "钱包提现到微信"),


	;
	final String value;
	final String desc;

	WithdrawTypeEnum(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}
}
