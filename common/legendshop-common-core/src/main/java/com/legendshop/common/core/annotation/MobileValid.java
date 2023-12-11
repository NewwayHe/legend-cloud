/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.annotation;

import com.legendshop.common.core.validator.MobileValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 手机号码校验
 *
 * @author legendshop
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {MobileValidator.class})
public @interface MobileValid {


	/**
	 * 错误提示信息
	 *
	 * @return
	 */
	String message() default "手机号码为空或格式错误";

	/**
	 * 作用参考@Validated和@Valid的区别
	 *
	 * @return
	 */
	Class<?>[] groups() default {};


	Class<? extends Payload>[] payload() default {};

	/**
	 * 自定义正则表达式
	 *
	 * @return
	 */
	String regexp() default "";

}
