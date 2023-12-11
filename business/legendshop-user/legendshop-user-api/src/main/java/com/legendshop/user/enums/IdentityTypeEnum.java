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
 * 身份类型
 *
 * @author legendshop
 */
public enum IdentityTypeEnum implements StringEnum {

	/**
	 * 平台
	 */
	PLATFORM("PLATFORM"),


	/**
	 * 商家
	 */
	SHOP("SHOP"),

	/**
	 * 用户
	 */
	USER("USER");


	private String value;

	IdentityTypeEnum(String value) {
		this.value = value;
	}

	@Override
	public String value() {
		return value;
	}

}
