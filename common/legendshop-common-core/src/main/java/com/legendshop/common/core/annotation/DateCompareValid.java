/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.annotation;

import com.legendshop.common.core.validator.DateCompareValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.*;

import static com.legendshop.common.core.annotation.DateCompareValid.DateCompareType.AFTER;

/**
 * 时间校验
 *
 * @author legendshop
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {DateCompareValidator.class})
public @interface DateCompareValid {


	/**
	 * 错误提示信息
	 */
	String message() default "时间为空或格式错误";


	/**
	 * 作用参考@Validated和@Valid的区别
	 */
	Class<?>[] groups() default {};


	Class<? extends Payload>[] payload() default {};

	/**
	 * 比较对象注解
	 */
	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@interface DateCompareTarget {

		/**
		 * 目标字段
		 */
		@NotNull
		String targetField();

		/**
		 * 比较类型，after或者before
		 * after为之前，before为之后
		 */
		DateCompareType type() default AFTER;

	}

	@Getter
	@AllArgsConstructor
	enum DateCompareType {
		/**
		 * 之后
		 */
		AFTER,

		/**
		 * 之前
		 */
		BEFORE

	}
}
