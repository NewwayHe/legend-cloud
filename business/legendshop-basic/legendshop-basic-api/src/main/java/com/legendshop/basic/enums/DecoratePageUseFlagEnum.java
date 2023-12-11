/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;

import com.legendshop.common.core.enums.IntegerEnum;

/**
 * 装修页面使用状态
 *
 * @author legendshop
 */
public enum DecoratePageUseFlagEnum implements IntegerEnum {

	/**
	 * 已使用
	 */
	USED(1),

	/**
	 * 未使用
	 */
	UNUSED(0),

	;

	private Integer num;

	@Override
	public Integer value() {
		// TODO Auto-generated method stub
		return num;
	}

	DecoratePageUseFlagEnum(Integer num) {
		this.num = num;
	}

}
