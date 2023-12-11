/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 已存在绑定帐号异常
 *
 * @author legendshop
 */
public class ExistBindAcountException extends AuthenticationException {

	public ExistBindAcountException(String message) {
		super(message);
	}
}
