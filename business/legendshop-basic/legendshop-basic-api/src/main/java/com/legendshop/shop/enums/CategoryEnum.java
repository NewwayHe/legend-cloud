/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.enums;

import lombok.Getter;

/**
 * 页面类型装修页面类型
 *
 * @author legendshop
 */
@Getter
public enum CategoryEnum {

	/**
	 * 首页
	 */
	INDEX_T("INDEX-T"),

	/**
	 * 海报
	 */
	POSTER_T("POSTER-T"),


	;

	final String value;

	CategoryEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
