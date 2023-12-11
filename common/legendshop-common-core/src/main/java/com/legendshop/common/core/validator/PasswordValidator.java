/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.validator;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.legendshop.common.core.annotation.PasswordValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 密码认证
 *
 * @author legendshop
 */
public class PasswordValidator implements ConstraintValidator<PasswordValid, String> {

	private PasswordValid annotation;
	/**
	 * 最短
	 */
	private final static Integer MIN = 6;
	/**
	 * 最长
	 */
	private final static Integer MAX = 16;
	/**
	 * 支持键盘上的数字,字母和特殊字符
	 */
	private final static String regex = "^[a-zA-Z0-9|`|~|!|@|#|$|%|^|&|*|(|)|_|+|-|=|<|>|?|,|.|/|\\]{0,}$";

	@Override
	public void initialize(PasswordValid constraintAnnotation) {
		this.annotation = constraintAnnotation;
	}

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		if (StrUtil.isBlank(password)) {
			return Boolean.FALSE;
		}
		return passwordValid(password);
	}

	/**
	 * 密码校验
	 */
	private Boolean passwordValid(String password) {
		boolean result = false;
		switch (annotation.type()) {
			case GENERAL:
				//5-16位英文数字下划线
				//正则检查
				if (Validator.isGeneral(password, MIN - 1, MAX)) {
					result = Boolean.TRUE;
				}
//	            if (Validator.isGeneral(password, MIN-1, MAX) && ReUtil.isMatch("",password)) {
//					result = Boolean.TRUE;
//				}
				break;
			case PAY:
				//判断是6位数字
				if (Validator.isNumber(password) && Validator.isGeneral(password, MIN)) {
					result = Boolean.TRUE;
				}
				break;
			default:
				break;
		}
		return result;
	}
}
