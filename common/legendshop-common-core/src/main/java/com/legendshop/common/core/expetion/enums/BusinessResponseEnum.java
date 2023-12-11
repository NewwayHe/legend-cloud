/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.expetion.enums;


import com.legendshop.common.core.expetion.BusinessExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum BusinessResponseEnum implements BusinessExceptionAssert {


	/**
	 * 业务异常
	 */
	BUSINESS_EXCEPTION(-1, "业务异常，请稍后重试"),


	;

	/**
	 * 返回码
	 */
	private final int code;
	/**
	 * 返回消息
	 */
	private final String msg;
}
