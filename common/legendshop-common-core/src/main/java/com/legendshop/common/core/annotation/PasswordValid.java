/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.annotation;

import com.legendshop.common.core.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.*;

/**
 * 密码校验
 *
 * @author legendshop
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {PasswordValidator.class})
public @interface PasswordValid {


	/**
	 * 错误提示信息
	 *
	 * @return
	 */
	String message() default "密码只支持数字和字母";


	/**
	 * 作用参考@Validated和@Valid的区别
	 *
	 * @return
	 */
	Class<?>[] groups() default {};


	Class<? extends Payload>[] payload() default {};

	/**
	 * 密码类型
	 *
	 * @return
	 */
	PasswordType type() default PasswordType.GENERAL;

	@AllArgsConstructor
	@Getter
	enum PasswordType {

		/**
		 * 通用密码6-16为英文字母 、数字和下划线
		 */
		GENERAL,

		/**
		 * 支付密码校验是否为6位数字
		 */
		PAY;

	}

}
