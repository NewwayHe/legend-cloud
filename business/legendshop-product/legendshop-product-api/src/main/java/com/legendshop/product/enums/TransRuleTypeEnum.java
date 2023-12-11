/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.enums;

import com.legendshop.common.core.enums.IntegerEnum;
import lombok.Getter;

/**
 * 店铺运费规则
 *
 * @author legendshop
 */
@Getter
public enum TransRuleTypeEnum implements IntegerEnum {

	/**
	 * 叠加计算
	 */
	CAL_ADD(1),

	/**
	 * 按最高值计算
	 */
	CAL_MAX(2);

	private Integer value;

	TransRuleTypeEnum(Integer value) {
		this.value = value;
	}

	@Override
	public Integer value() {
		return value;
	}
}
