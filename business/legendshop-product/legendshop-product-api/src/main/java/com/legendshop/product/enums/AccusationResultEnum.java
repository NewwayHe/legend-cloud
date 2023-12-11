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
 * 举报结果
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum AccusationResultEnum implements IntegerEnum {
	/**
	 * 无效举报
	 */
	INVALID(-1),
	/**
	 * 有效举报
	 */
	VALID(1),

	/**
	 * 恶意举报
	 */
	TERRIBLE(-2);

	private Integer value;

	@Override
	public Integer value() {
		return value;
	}
}
