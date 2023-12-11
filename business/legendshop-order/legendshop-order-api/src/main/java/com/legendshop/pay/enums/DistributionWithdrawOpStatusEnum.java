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
public enum DistributionWithdrawOpStatusEnum {

	AUDIT_FAIL(-1, "提现审核失败"),
	AUDIT(0, "提现待审核"),
	AUDIT_SUCCESS(1, "提现审核完成"),
	;

	final Integer value;

	final String desc;

	DistributionWithdrawOpStatusEnum(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}
}
