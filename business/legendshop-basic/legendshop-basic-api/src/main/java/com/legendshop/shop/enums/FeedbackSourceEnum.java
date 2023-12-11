/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.enums;

import com.legendshop.common.core.enums.StringEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户反馈来源枚举
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum FeedbackSourceEnum implements StringEnum {

	/**
	 * pc端
	 */
	PC("pc"),

	/**
	 * mobile端
	 */
	MOBILE("mobile"),

	;

	/**
	 * The value.
	 */
	private final String value;

	@Override
	public String value() {
		return this.value;
	}
}
