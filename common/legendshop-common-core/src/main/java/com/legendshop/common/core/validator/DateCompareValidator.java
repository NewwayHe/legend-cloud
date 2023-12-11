/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.validator;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.legendshop.common.core.annotation.DateCompareValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

import static com.legendshop.common.core.annotation.DateCompareValid.DateCompareTarget;
import static com.legendshop.common.core.annotation.DateCompareValid.DateCompareType;

/**
 * 日期比较字段校验
 *
 * @author legendshop
 */
@Slf4j
public class DateCompareValidator implements ConstraintValidator<DateCompareValid, Object> {

	private DateCompareValid annotation;

	private final static String GETTER = "get";

	@Override
	public void initialize(DateCompareValid constraintAnnotation) {
		this.annotation = constraintAnnotation;
	}

	@SneakyThrows
	@Override
	public boolean isValid(Object obj, ConstraintValidatorContext context) {
		Class<?> objClass = obj.getClass();
		Field[] declaredFields = ClassUtil.getDeclaredFields(objClass);
		for (Field field : declaredFields) {
			DateCompareTarget targetAnnotation = field.getAnnotation(DateCompareTarget.class);
			if (targetAnnotation == null) {
				continue;
			}
			@NotNull String targetField = targetAnnotation.targetField();
			try {
				//获取目标方法的值
				String targetMethodName = StrUtil.upperFirstAndAddPre(targetField, GETTER);
				Method targetMethod = objClass.getMethod(targetMethodName);
				Date targetTime = (Date) targetMethod.invoke(obj);

				//获取当前字段的值
				String fieldName = field.getName();
				String fieldMethodName = StrUtil.upperFirstAndAddPre(fieldName, GETTER);
				Method currentMethod = objClass.getMethod(fieldMethodName);
				Date currentTime = (Date) currentMethod.invoke(obj);
				//比较当前日期是否在目标日期之前
				DateCompareType type = targetAnnotation.type();
				if (DateCompareType.BEFORE.equals(type)) {
					if (!currentTime.before(targetTime)) {
						return Boolean.FALSE;
					}
				} else {
					if (!currentTime.after(targetTime)) {
						return Boolean.FALSE;
					}
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				return Boolean.FALSE;
			}

		}
		return Boolean.TRUE;
	}
}
