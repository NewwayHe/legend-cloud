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
 * 商品限购类型
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum ProductQuotaTypeEnum {

	/*每单限购*/
	ORDER("O"),

	/*每日限购*/
	DAY("D"),

	/*每周限购*/
	WEEK("W"),

	/*每月限购*/
	MONTH("M"),

	/*每年限购*/
	YEAR("Y"),

	/*终身限购*/
	ALWAYS("A");

	/**
	 * The value.
	 */
	private final String value;
}
