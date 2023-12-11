/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;


import com.legendshop.common.core.enums.StringEnum;

/**
 * 装修页面类型
 *
 * @author legendshop
 */
public enum DecoratePageCategoryEnum implements StringEnum {

	/**
	 * 首页
	 */
	INDEX("INDEX"),

	/**
	 * 海报
	 */
	POSTER("POSTER"),

	/**
	 * 首页
	 */
	INDEX_T("INDEX_T"),

	/**
	 * 海报
	 */
	POSTER_T("POSTER_T"),

	/**
	 * 个人中心
	 */
	PERSONAL_CENTER("PERSONAL_CENTER"),

	/**
	 * 分销中心
	 */
	DISTRI_CENTER("DISTRI_CENTER"),

	;

	/**
	 * The value.
	 */
	private final String value;

	/**
	 * @param value
	 */
	DecoratePageCategoryEnum(String value) {
		this.value = value;
	}


	@Override
	public String value() {
		return this.value;
	}

}
