/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.enums;


import com.legendshop.common.core.enums.StringEnum;

/**
 * 系统角色,不可以在页面上进行修改,要在数据库中定义好对应的ID值
 *
 * @author legendshop
 */
public enum RoleEnum implements StringEnum {

	/**
	 * 超级管理员
	 */
	ROLE_ADMIN("1"),

	/**
	 * 普通用户
	 */
	ROLE_USER("1"),

	/**
	 * 商家用户
	 */
	ROLE_SUPPLIER("1"),

	/**
	 * 普通管理员
	 */
	ORDINARY_ROLE("101");

	private final String value;

	private RoleEnum(String value) {
		this.value = value;
	}

	@Override
	public String value() {
		return this.value;
	}

	public static boolean instance(String name) {
		RoleEnum[] licenseEnums = values();
		for (RoleEnum licenseEnum : licenseEnums) {
			if (licenseEnum.name().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public static String getValue(String name) {
		RoleEnum[] licenseEnums = values();
		for (RoleEnum licenseEnum : licenseEnums) {
			if (licenseEnum.name().equals(name)) {
				return licenseEnum.value();
			}
		}
		return null;
	}
}
