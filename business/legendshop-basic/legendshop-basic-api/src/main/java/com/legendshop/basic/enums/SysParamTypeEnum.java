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
 * 系统主配置类型
 *
 * @author legendshop
 */
public enum SysParamTypeEnum implements IntegerEnum {
	/**
	 * 第三方配置
	 */
	THIRD_PLATE(10),

	/**
	 * 业务配置
	 */
	BUSINESS(20),

	/**
	 * 系统配置
	 */
	SYSTEM(30),
	;

	private Integer value;

	SysParamTypeEnum(Integer value) {
		this.value = value;
	}

	@Override
	public Integer value() {
		return value;
	}

	public Integer getValue() {
		return value;
	}
}
