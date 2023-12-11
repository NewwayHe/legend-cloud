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
 * 提现账号默认枚举
 *
 * @author legendshop
 */
@Getter
public enum AccountDefaultEnum {
	UN_DEFAULT(0, "没默认"),
	DEFAULT(1, "默认"),

	;
	final Integer code;
	final String desc;

	AccountDefaultEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}
}
