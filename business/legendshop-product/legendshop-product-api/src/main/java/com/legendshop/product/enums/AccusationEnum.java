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
 * 举报状态
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum AccusationEnum implements IntegerEnum {

	/**
	 * 已处理
	 */
	STATUS_YES(1),

	/**
	 * 未处理
	 */
	STATUS_NO(0),

	/**
	 * 已删除
	 */
	DEL_STATUS_YES(1),

	/**
	 * 未删除
	 */
	DEL_STATUS_NO(0);

	private final Integer value;

	@Override
	public Integer value() {
		return value;
	}
}
