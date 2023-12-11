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
public enum PaySettlementStateEnum {

	EXPIRED(-1, "过期、作废、退款"),

	CREATE(0, "创建，未处理"),

	PAID(1, "已支付"),

	CLEARING(2, "清算完成，结束"),

	EXCEPTION(3, "支付异常，重复支付"),

	;

	final Integer code;

	final String desc;

	PaySettlementStateEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}
}
