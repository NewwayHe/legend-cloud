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
 * 应用类型
 * 见表 ls_cst_table 和 ls_cas_application 定义
 *
 * @author legendshop
 */
public enum ApplicationEnum implements StringEnum {
	/**
	 * 前台
	 */
	FRONT_END("FRONT_END"),

	/**
	 * 后台
	 */
	BACK_END("BACK_END");

	private final String value;

	private ApplicationEnum(String value) {
		this.value = value;
	}

	@Override
	public String value() {
		return this.value;
	}

	public static boolean instance(String name) {
		ApplicationEnum[] enums = values();
		for (ApplicationEnum appEnum : enums) {
			if (appEnum.name().equals(name)) {
				return true;
			}
		}
		return false;
	}

}
