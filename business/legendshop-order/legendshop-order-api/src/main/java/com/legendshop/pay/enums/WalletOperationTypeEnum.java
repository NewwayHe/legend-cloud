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
 * 用户钱包收支处理类型
 *
 * @author legendshop
 */
@Getter
public enum WalletOperationTypeEnum {
	DEDUCTION(0, "扣款"),
	ADDITION(1, "添加"),
	;

	final Integer code;
	final String desc;

	WalletOperationTypeEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}
}
