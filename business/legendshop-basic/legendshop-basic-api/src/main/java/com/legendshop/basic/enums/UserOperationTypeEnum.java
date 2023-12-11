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
 * 用户操作类型
 *
 * @author legendshop
 */
public enum UserOperationTypeEnum {
	/**
	 * 按钮提交
	 */
	BUTTON("按钮提交"),

	/**
	 * 页面跳转
	 */
	PAGE("页面跳转");


	final String desc;

	UserOperationTypeEnum(String desc) {
		this.desc = desc;
	}
}
