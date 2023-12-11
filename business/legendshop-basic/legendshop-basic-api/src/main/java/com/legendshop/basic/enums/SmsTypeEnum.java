/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;

import com.legendshop.common.core.enums.IntegerEnum;

/**
 * 短信发送类型类型
 * <p>
 * ----------------------------------------------------------------------------.
 *
 * @author legendshop
 */
public enum SmsTypeEnum implements IntegerEnum {

	/**
	 * 验证
	 */
	VAL(10),

	/**
	 * 用户
	 */
	USER(20),

	/**
	 * 商品
	 */
	PROD(30),

	/**
	 * 订单
	 */
	ORDER(40),

	/**
	 * 营销
	 */
	MARKETING(50),

	/**
	 * 系统
	 */
	SYSTEM(90),

	;
	final Integer code;

	public SmsTypeEnum getEnum(Integer code) {
		for (SmsTypeEnum value : values()) {
			if (value.code.equals(code)) {
				return value;
			}
		}
		return null;
	}

	SmsTypeEnum(Integer code) {
		this.code = code;
	}

	@Override
	public Integer value() {
		return this.code;
	}
}
