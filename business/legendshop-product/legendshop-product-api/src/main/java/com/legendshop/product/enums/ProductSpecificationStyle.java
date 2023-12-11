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
 * 规格样式 PIC：图片样式；TXT：文本样式
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum ProductSpecificationStyle {

	/*图片样式*/
	PICTURE("PIC"),

	/*文本样式(无图样式)*/
	TXT("TXT"),

	/*图文样式*/
	GRAPHIC("GRAPHIC");

	/**
	 * The value.
	 */
	private final String value;

}
