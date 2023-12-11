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
public enum WalletDetailsStateEnum {
	ABNORMAL(-999, "异常错误"),
	INVALID(-1, "失效"),
	DEFAULT(0, "默认"),
	FULFILL(1, "完成"),
	PROCESSING(2, "处理中"),
	;

	final Integer code;
	final String desc;

	WalletDetailsStateEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static WalletDetailsStateEnum getInstance(Integer code) {
		for (WalletDetailsStateEnum moneyStatusEnum : WalletDetailsStateEnum.values()) {
			if (moneyStatusEnum.getCode().equals(code)) {
				return moneyStatusEnum;
			}
		}
		return null;
	}
}
