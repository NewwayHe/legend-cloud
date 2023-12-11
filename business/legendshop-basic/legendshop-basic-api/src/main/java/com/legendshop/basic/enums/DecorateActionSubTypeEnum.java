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
 * 装修点击动作子类型
 *
 * @author legendshop
 */
public enum DecorateActionSubTypeEnum implements StringEnum {

	/**
	 * 商品列表 - 分类
	 */
	CATEGORY("category"),

	/**
	 * 商品列表 - 品牌
	 */
	BRAND("BRAND"),

	/**
	 * 商品列表 - 标签
	 */
	TAGS("tags"),

	/**
	 * 商品列表 - 关键字
	 */
	KEYWORD("keyword"),

	/**
	 * 自定义URL - 跳转网页
	 */
	WEB("web"),

	/**
	 * 自定义URL - 小程序页面
	 */
	MP("mp"),

	/**
	 * 自定义URL - 外部链接
	 */
	OUT_URL("outURL");

	private final String value;

	DecorateActionSubTypeEnum(String value) {
		this.value = value;
	}

	@Override
	public String value() {
		return value;
	}
}
