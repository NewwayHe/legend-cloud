/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;

import lombok.Getter;

/**
 * @author legendshop
 */
@Getter
public enum SignTypeEnum {
	/**
	 * 连续签到
	 */
	CONTINUOUS(1),

	/**
	 * 周期签到
	 */
	PERIOD(2),
	;


	private Integer value;

	SignTypeEnum(Integer value) {
		this.value = value;
	}
}
