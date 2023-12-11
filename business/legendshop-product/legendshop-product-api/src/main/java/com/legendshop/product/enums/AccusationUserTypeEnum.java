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
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 举报人类型（1、用户 2、平台(管理员））
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum AccusationUserTypeEnum implements IntegerEnum {

	/**
	 * 用户
	 */
	USER(1),

	/**
	 * 平台(管理员）
	 */
	ADMIN(2);

	private final Integer value;

	@Override
	public Integer value() {
		return value;
	}
}
