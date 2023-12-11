/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.annotation;


import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.legendshop.common.core.sensitive.SensitiveDataSerialize;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据脱敏注解
 *
 * @author legendshop
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveDataSerialize.class)
public @interface DataSensitive {

	@NotNull
	SensitiveTypeEnum type();


	/**
	 * 脱敏枚举类
	 */
	enum SensitiveTypeEnum {
		/**
		 * 手机号, 138****8000
		 */
		MOBILE_PHONE,

		/**
		 * 密码, ************
		 */
		PASSWORD,

		/**
		 * 身份证号码, 123456********0000
		 */
		ID_CARD,

		/**
		 * 银行卡, 6217 **** **** **** 567
		 */
		BANK_CARD

	}
}
