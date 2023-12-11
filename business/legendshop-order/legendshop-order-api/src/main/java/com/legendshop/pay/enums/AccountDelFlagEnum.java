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
 * 提现账号是否删除枚举
 *
 * @author legendshop
 */
@Getter
public enum AccountDelFlagEnum {
	WAIT(0, "未删除"),
	SUCCESS(1, "已删除"),

	;
	final Integer code;
	final String desc;

	AccountDelFlagEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}
}
