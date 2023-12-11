/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.log.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.lang.annotation.*;

/**
 * 记录操作日志的注解
 *
 * @author legendshop
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {

	/**
	 * 日志标题
	 */
	@Nullable
	String value() default "";

	/**
	 * 日志类型，分开记录admin和shop日志
	 */
	LogType type() default LogType.ADMIN;


	@Getter
	@AllArgsConstructor
	enum LogType {

		/**
		 * 后台日志
		 */
		ADMIN("admin"),

		/**
		 * 商家端日志
		 */
		SHOP("shop"),

		/**
		 * 用户端日志
		 */
		USER("user");


		final String type;
	}
}
