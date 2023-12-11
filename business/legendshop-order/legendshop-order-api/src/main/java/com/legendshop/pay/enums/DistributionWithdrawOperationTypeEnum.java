/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 分销钱包操作类型
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum DistributionWithdrawOperationTypeEnum {

	DEDUCTION(0, "扣款"),
	ADDITION(1, "添加"),

	;

	final Integer code;
	final String desc;

	public static String fromDesc(String name) {
		if (StrUtil.isBlank(name)) {
			return "";
		}

		for (DistributionWithdrawOperationTypeEnum value : DistributionWithdrawOperationTypeEnum.values()) {
			if (value.name().equals(name)) {
				return value.getDesc();
			}
		}

		return "";
	}
}
