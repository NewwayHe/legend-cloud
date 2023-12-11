/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.security.enums;

import com.legendshop.common.security.exception.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;

/**
 * @author legendshop
 */
@Slf4j
@Getter
public enum LegendshopSecurityErrorEnum {

	/**
	 * *
	 */
	ERROR_AUTH_TYPE("错误的认证类型", new ErrorAuthTypeException("错误的认证类型")),

	BAD_CREDENTIALS("用户名或密码错误", new BadCredentialsException("用户名或密码错误")),

	VERIFY_CODE_ERROR("验证码错误", new BadCredentialsException("验证码错误")),

	THIRD_PARTY_BIND_ERROR("第三方绑定错误", new BadCredentialsException("第三方绑定错误")),

	THIRD_PARTY_AUTH_ERROR("第三方认证错误", new BadCredentialsException("第三方认证错误")),

	NOT_BIND_USER("用户未绑定", new NotBindUserException("用户未绑定")),

	USER_NOT_FOUND("用户名或密码错误", new UserNotFoundException("用户名或密码错误")),

	UN_REGISTERED("用户未注册", new UnRegisteredException("用户未注册")),

	EXIST_BIND_ACCOUNT("用户已绑定账号", new ExistBindAcountException("用户已绑定账号"));
	final String name;
	final RuntimeException exception;

	LegendshopSecurityErrorEnum(String name, RuntimeException exception) {
		this.name = name;
		this.exception = exception;
	}

	public static LegendshopSecurityErrorEnum codeValue(String name) {
		for (LegendshopSecurityErrorEnum value : LegendshopSecurityErrorEnum.values()) {
			if (value.name().equals(name)) {
				return value;
			}
		}
		log.info("登录认证失败, {}", name);
		throw new BadCredentialsException("登录认证失败！");
	}
}
