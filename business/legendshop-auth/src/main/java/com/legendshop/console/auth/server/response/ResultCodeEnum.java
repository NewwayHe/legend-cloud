/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.console.auth.server.response;

import lombok.Getter;

/**
 * 自定义认证服务返回Code值
 *
 * @author legendshop
 */
@Getter
public enum ResultCodeEnum {

	/**
	 *
	 */
	SUCCESS(200, "认证成功"),
	/**
	 *
	 */
	FAIL(400, "认证失败");

	private final Integer code;
	private final String message;

	ResultCodeEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
}
