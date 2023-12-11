/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

/**
 * @author legendshop
 */
public class NotBindUserException extends AuthenticationException {

	private static final long serialVersionUID = -1594283426604619878L;

	public NotBindUserException(String message) {
		super(message);
	}

	public int getHttpErrorCode() {
		return HttpStatus.CREATED.value();
	}
}
