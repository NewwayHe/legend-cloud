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
 * 钱包中间表状态
 * 状态（0: 不处理，10: 未处理（未通知），20: 未处理（已通知），30: 已处理）
 *
 * @author legendshop
 */
@Getter
public enum WalletCentreStatusEnum {

	DO_NOT_HANDLE(0, "不处理"),
	NOT_NOTIFIED(10, "待通知"),
	NOT_PROCESSED(20, "待处理"),
	SUCCESS(30, "处理完成"),

	;
	final Integer value;
	final String desc;

	WalletCentreStatusEnum(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}
}
