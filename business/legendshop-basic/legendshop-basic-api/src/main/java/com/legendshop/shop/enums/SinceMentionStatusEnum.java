/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 自提点状态
 *
 * @author legendshop
 */
@Getter
public enum SinceMentionStatusEnum {
	/**
	 * 开启
	 */
	TURNON("1"),

	/**
	 * 关闭
	 */
	CLOSURE("10");
	private String code;


	SinceMentionStatusEnum(String code) {
		this.code = code;
	}

	public static SinceMentionStatusEnum findByCode(String code) {
		return Arrays.stream(SinceMentionStatusEnum.values()).filter((item) -> item.getCode().equals(code)).findFirst().orElse(null);
	}
}
