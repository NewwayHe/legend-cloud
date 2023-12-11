/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.enums;

/**
 * 首页装修设置枚举
 *
 * @author legendshop
 */
public enum DecorateSettingEnum {

	/**
	 * 类目级别
	 */
	GRADE_ONE("DISPLAY_CATEGORY_GRADE", 1, "一级分类"),

	GRADE_TWO("DISPLAY_CATEGORY_GRADE", 2, "二级分类"),

	GRADE_THREE("DISPLAY_CATEGORY_GRADE", 3, "三级分类"),


	/**
	 * 有列表
	 */
	HAS_IMAGE("DISPLAY_CATEGORY_IMAGE", 0, "无图模式"),

	HAS_NOT_IMAGE("DISPLAY_CATEGORY_IMAGE", 1, "有图模式"),

//	HAS_IMAGE_NOT_LIST("DISPLAY_CATEGORY_IMAGE", 2, "有图无列表模式"),


	/**
	 * 分类广告
	 */
	HAS_NOT_ADVERT("DISPLAY_CATEGORY_ADVERT", 0, "不展示分类广告"),

	HAS_ADVERT("DISPLAY_CATEGORY_ADVERT", 1, "展示分类广告"),


	/**
	 * 是否有商品
	 */
	HAS_GOODS("DISPLAY_CATEGORY_GOODS", 1, "展示商品"),

	HAS_NOT_GOODS("DISPLAY_CATEGORY_GOODS", 0, "不展示商品"),


	;
	private String key;

	private Integer value;

	private String des;

	DecorateSettingEnum(String key, Integer value, String des) {
		this.key = key;
		this.value = value;
		this.des = des;
	}

	public String getKey() {
		return key;
	}

	public Integer getValue() {
		return value;
	}

	public String getDes() {
		return des;
	}
}
