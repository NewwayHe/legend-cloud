/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.annotation;

import com.legendshop.common.core.validator.EnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

/**
 * 字段状态类型的枚举校验
 *
 * @author legendshop
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {EnumValidator.class})
public @interface EnumValid {

	/**
	 * 错误提示信息
	 *
	 * @return
	 */
	String message() default "枚举类型不匹配";

	/**
	 * 校验的目标字段，默认为value
	 *
	 * @return
	 */
	String targetField() default "value";

	/**
	 * 作用参考@Validated和@Valid的区别
	 *
	 * @return
	 */
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 目标枚举类
	 */
	@NotNull
	Class<?> target();

	/**
	 * 是否忽略空值
	 */
	boolean ignoreEmpty() default false;
}
