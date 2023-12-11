/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.enums;

/**
 * 操作佣金类型
 *
 * @author legendshop
 */
public enum DistributionWalletCommissionTypeEnum {

	/**
	 * 数据库的操作类型
	 */
	FROZEN_COMMISSION("冻结金额"),
	SETTLED_COMMISSION("已结算金额"),
	;

	final String desc;

	DistributionWalletCommissionTypeEnum(String desc) {
		this.desc = desc;
	}
}
