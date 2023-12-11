/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.enums;

/**
 * 系统缓存的对象
 *
 * @author legendshop
 */
public enum SystemCacheEnum implements StringEnum {
	/**
	 * 分类树
	 */
	CATEGORY("CATEGORY"),

	/**
	 * 属性
	 */
	PROPERTIES("PROPERTIES");

	private final String value;

	private SystemCacheEnum(String value) {
		this.value = value;
	}

	@Override
	public String value() {
		return this.value;
	}

	public static boolean instance(String name) {
		SystemCacheEnum[] enums = values();
		for (SystemCacheEnum appEnum : enums) {
			if (appEnum.name().equals(name)) {
				return true;
			}
		}
		return false;
	}

}
