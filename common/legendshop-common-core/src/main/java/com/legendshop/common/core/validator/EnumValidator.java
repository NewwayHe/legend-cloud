/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.validator;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.legendshop.common.core.annotation.EnumValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 枚举字段校验
 *
 * @author legendshop
 */
@Slf4j
public class EnumValidator implements ConstraintValidator<EnumValid, Object> {

	/**
	 * 枚举注解
	 */
	private EnumValid annotation;

	private final static String GETTER = "get";

	@Override
	public void initialize(EnumValid constraintAnnotation) {
		this.annotation = constraintAnnotation;
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		boolean ignoreEmpty = annotation.ignoreEmpty();
		if (ObjectUtil.isEmpty(value)) {
			if (ignoreEmpty) {
				return Boolean.TRUE;
			}

			log.warn("参数为空");
			return Boolean.FALSE;
		}
		Class<?> targetClass = annotation.target();
		if (!targetClass.isEnum()) {
			return Boolean.TRUE;
		}
		try {
			Object[] objects = targetClass.getEnumConstants();
			// 字段名称必须为value，并且提供get方法
			String methodName = StrUtil.upperFirstAndAddPre(annotation.targetField(), GETTER);
			Method method = targetClass.getMethod(methodName);
			for (Object obj : objects) {
				Object code = method.invoke(obj);
				if (ObjectUtil.equal(value, code)) {
					return Boolean.TRUE;
				}
			}
			return Boolean.FALSE;
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			log.warn("EnumValidator call 'GET' method exception.");
			return Boolean.FALSE;

		}
	}
}
