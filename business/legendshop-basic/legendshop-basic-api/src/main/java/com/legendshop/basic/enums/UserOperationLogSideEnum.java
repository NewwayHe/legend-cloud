/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;

/**
 * 用户操作端
 *
 * @author legendshop
 */
public enum UserOperationLogSideEnum {
	/**
	 * 平台
	 */
	ADMIN("平台"),

	/**
	 * 商家PC端
	 */
	SHOP_PC("商家PC端"),

	/**
	 * 商家移动端
	 */
	SHOP_MOBILE("商家移动端"),

	/**
	 * 用户PC端
	 */
	USER_PC("用户PC端"),

	/**
	 * 用户移动端
	 */
	USER_MOBILE("用户移动端");

	final String side;

	UserOperationLogSideEnum(String side) {
		this.side = side;
	}
}
