/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.enums;


import com.legendshop.common.core.enums.IntegerEnum;

/**
 * 营销活动 计算类型：0:按金额  1：按件.
 *
 * @author legendshop
 */
public enum MarketingRuleCalTypeEnum implements IntegerEnum {

	/**
	 * 按金额
	 */
	BY_MONEY(0),

	/**
	 * 按件数
	 */
	BY_NUMBER(1);


	/**
	 * The num.
	 */
	private Integer num;

	@Override
	public Integer value() {
		return num;
	}

	MarketingRuleCalTypeEnum(Integer num) {
		this.num = num;
	}

}
