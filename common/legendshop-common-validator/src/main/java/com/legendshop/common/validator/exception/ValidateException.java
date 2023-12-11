/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.validator.exception;

/**
 * @author legendshop
 */
public class ValidateException extends RuntimeException {

	private static final long serialVersionUID = -5288215958587498529L;

	public ValidateException() {
	}

	public ValidateException(String message) {
		super(message);
	}

}
