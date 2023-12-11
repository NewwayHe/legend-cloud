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
 */
@Getter
@AllArgsConstructor
public enum WalletDetailsAuditEnum {
	WAIT(0, "待审核"),
	PASS(1, "通过"),
	REFUSED(-1, "拒绝");

	final Integer code;
	final String desc;
}
