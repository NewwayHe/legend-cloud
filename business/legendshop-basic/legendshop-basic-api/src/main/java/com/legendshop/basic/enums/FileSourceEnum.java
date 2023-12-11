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

/**
 * 文件源类型
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum FileSourceEnum {

	/**
	 * 平台
	 */
	ADMIN("A"),

	/**
	 * 商家
	 */
	SHOP("S"),

	/**
	 * 用户
	 */
	USER("U");

	private String value;

	public static boolean contains(String value) {
		for (FileSourceEnum typeEnum : FileSourceEnum.values()) {
			if (typeEnum.getValue().equals(value)) {
				return true;
			}
		}
		return false;
	}
}
