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
 * @author legendshop
 */
public enum ExceptionLogLevelEnum {
	/**
	 * 无法处理，需要人工干预
	 */
	EMERGENCY(9999, "无法处理，需要人工干预"),

	/**
	 * 错误
	 */
	ERROR(2, "错误"),

	/**
	 * 警告
	 */
	WARNING(1, "警告");


	final Integer level;

	final String desc;

	ExceptionLogLevelEnum(Integer level, String desc) {
		this.level = level;
		this.desc = desc;
	}

	public Integer level() {
		return this.level;
	}
}
