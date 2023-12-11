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
public enum WalletStateEnum {
	ABNORMAL(-999, "异常冻结"),
	INVALID(-1, "拒绝"),
	DEFAULT(0, "失效"),
	SUCCESS(1, "默认");

	final Integer code;
	final String desc;

	WalletStateEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}
}
