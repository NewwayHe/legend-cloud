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

/**
 * 配送区域类型
 *
 * @author legendshop
 */
public enum TransCityTypeEnum implements IntegerEnum {
	/**
	 * 区域限售
	 */
	AREA_LIMIT(1),

	/**
	 * 运费计算
	 */
	FREIGHT_CAL(2),

	/**
	 * 条件包邮
	 */
	CONDITION_FREE(3),

	/**
	 * 固定运费
	 */
	CONSTANT_FREIGHT(4);

	private Integer value;

	TransCityTypeEnum(Integer value) {
		this.value = value;
	}

	@Override
	public Integer value() {
		return value;
	}
}
