/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商品类型Enum E.实物商品、V:虚拟商品
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum ProductTypeEnum {

	/**
	 * 实物商品
	 */
	ENTITY("E"),

	/**
	 * 虚拟商品
	 */
	VIRTUAL("V");

	private final String value;


	public String value() {
		return value;
	}
}
