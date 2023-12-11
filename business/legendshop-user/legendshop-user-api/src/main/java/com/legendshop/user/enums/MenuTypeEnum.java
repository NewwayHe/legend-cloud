/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 菜单的类型
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum MenuTypeEnum {

	/**
	 * 左侧菜单
	 */
	LEFT_MENU("0", "left"),

	/**
	 * 按钮
	 */
	BUTTON("1", "button"),

	/**
	 * 顶部菜单
	 */
	TOP_MENU("2", "top");

	/**
	 * 类型
	 */
	private final String type;

	/**
	 * 描述
	 */
	private final String description;

}
