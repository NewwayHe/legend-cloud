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
public enum DistributionWithdrawBusinessTypeEnum {

	/**
	 * 提现审核业务
	 */
	AUDIT("提现待审核"),
	AUDIT_FAIL("提现审核失败"),
	AUDIT_SUCCESS("提现审核完成"),

	/**
	 * 实际提现业务
	 */
	WITHDRAW_FAIL("提现失败"),
	WITHDRAW_SUCCESS("提现完成"),


	/**
	 * 自购佣金
	 */
	SELF_COMMISSION("自购佣金"),
	/**
	 * 二级佣金
	 */
	SECOND_LEVEL_COMMISSION("二级佣金"),

	/**
	 * 三级佣金
	 */
	THIRD_LEVEL_COMMISSION("三级佣金"),

	/**
	 * 二级推荐奖励
	 */
	SECOND_PROMOTION_REWARD("二级推荐奖励"),

	/**
	 * 三级推荐奖励
	 */
	THIRD_PROMOTION_REWARD("三级推荐奖励"),

	/**
	 * 拉新奖励
	 */
	PULL_NEW("拉新奖励"),
	;

	final String desc;

	DistributionWithdrawBusinessTypeEnum(String desc) {
		this.desc = desc;
	}

	public String getValue() {
		return this.name();
	}
}
