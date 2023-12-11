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
 * 商品分组类型
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum ProductGroupTypeEnum implements IntegerEnum {
	/**
	 * 系统定义
	 */
	SYSTEM(0),

	/**
	 * 自定义
	 */
	CUSTOMIZE(1);

	private Integer value;

	@Override
	public Integer value() {
		return value;
	}
}
