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
public enum PayRefundStateEnum {

	DEFAULT(0, "默认"),
	SUCCESS(1, "完成"),
	FAIL(-1, "失败");
	final Integer code;

	final String desc;

	PayRefundStateEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}
}
