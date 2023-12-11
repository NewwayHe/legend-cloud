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
import com.legendshop.common.core.annotation.MobileValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * 手机号认证
 *
 * @author legendshop
 */
public class MobileValidator implements ConstraintValidator<MobileValid, String> {

	private MobileValid annotation;

	@Override
	public void initialize(MobileValid constraintAnnotation) {
		this.annotation = constraintAnnotation;
	}

	@Override
	public boolean isValid(String mobile, ConstraintValidatorContext context) {
		//手机号码为空
		if (StrUtil.isBlank(mobile)) {
			return Boolean.FALSE;
		}
		//手机号码长度不等于11为
		if (mobile.length() != 11) {
			return Boolean.FALSE;
		}
		//手机号码不匹配
		if (StrUtil.isBlank(annotation.regexp())) {
			return Validator.isMobile(mobile);
		}
		//手机号码不匹配自定义正则表达式
		Pattern pattern = Pattern.compile(annotation.regexp());
		return pattern.matcher(mobile).matches();
	}
}
