/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 地区等级枚举
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum LocationGradeEnum {

	/**
	 * 省级
	 */
	PROVINCE(1),

	/**
	 * 市级
	 */
	CITY(2),

	/**
	 * 区级
	 */
	AREA(3),

	/**
	 * 街道
	 */
	STREET(4);


	private Integer value;

	public static LocationGradeEnum findByValue(Integer value) {
		return Arrays.stream(LocationGradeEnum.values()).filter((item) -> item.getValue().equals(value)).findFirst().orElse(null);
	}
}
