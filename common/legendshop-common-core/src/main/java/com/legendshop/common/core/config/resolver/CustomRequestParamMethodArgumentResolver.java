/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.config.resolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;

/**
 * 自定义请求参数解析器
 *
 * @author legendshop
 */
@Slf4j
public class CustomRequestParamMethodArgumentResolver extends RequestParamMethodArgumentResolver {


	/**
	 * Create a new {@link CustomRequestParamMethodArgumentResolver} instance.
	 *
	 * @param useDefaultResolution in default resolution mode a method argument
	 *                             that is a simple type, as defined in {@link BeanUtils#isSimpleProperty},
	 *                             is treated as a request parameter even if it isn't annotated, the
	 *                             request parameter name is derived from the method parameter name.
	 */
	public CustomRequestParamMethodArgumentResolver(boolean useDefaultResolution) {
		super(useDefaultResolution);
	}

	/**
	 * Create a new {@link CustomRequestParamMethodArgumentResolver} instance.
	 *
	 * @param beanFactory          a bean factory used for resolving  ${...} placeholder
	 *                             and #{...} SpEL expressions in default values, or {@code null} if default
	 *                             values are not expected to contain expressions
	 * @param useDefaultResolution in default resolution mode a method argument
	 *                             that is a simple type, as defined in {@link BeanUtils#isSimpleProperty},
	 *                             is treated as a request parameter even if it isn't annotated, the
	 *                             request parameter name is derived from the method parameter name.
	 */
	public CustomRequestParamMethodArgumentResolver(@Nullable ConfigurableBeanFactory beanFactory,
													boolean useDefaultResolution) {

		super(beanFactory, useDefaultResolution);
	}

	@Override
	protected void handleMissingValue(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
		HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);
		if (null != servletRequest) {
			log.error("请求失败，找不到接口对应的请求参数，{}，参数类型：{}，参数名称：{}", servletRequest.getRequestURI(), parameter.getNestedParameterType().getSimpleName(), name);
		}
		super.handleMissingValue(name, parameter, request);
	}
}
