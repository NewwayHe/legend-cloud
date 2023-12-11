/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.enums;


import com.legendshop.common.core.enums.StringEnum;

/**
 * 运费模板计费方式
 *
 * @author legendshop
 */
public enum TransTypeEnum implements StringEnum {

	/**
	 * 按件数
	 */
	NUMBER("1"),

	/**
	 * 按重量
	 */
	WEIGHT("2"),

	/**
	 * 按体积
	 */
	VOLUME("3"),

	/**
	 * 固定运费
	 */
	CONSTANT("4");

	/**
	 * The value.
	 */
	private final String value;

	/**
	 * Instantiates a new visit type enum.
	 *
	 * @param value the value
	 */
	TransTypeEnum(String value) {
		this.value = value;
	}


	@Override
	public String value() {
		return this.value;
	}

	public static TransTypeEnum fromCode(String value) {
		if (null != value) {
			for (TransTypeEnum transTypeEnum : TransTypeEnum.values()) {
				if (transTypeEnum.value.equals(value)) {
					return transTypeEnum;
				}
			}
		}
		return null;
	}
}
