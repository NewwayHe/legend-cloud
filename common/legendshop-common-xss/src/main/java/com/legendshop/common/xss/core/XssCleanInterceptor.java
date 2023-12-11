/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.xss.core;

import com.legendshop.common.core.util.ClassUtil;
import com.legendshop.common.xss.annotation.XssCleanIgnore;
import com.legendshop.common.xss.properties.XssProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Xss拦截处理器
 *
 * @author legendshop
 */
@RequiredArgsConstructor
public class XssCleanInterceptor implements HandlerInterceptor {

	private final XssProperties xssProperties;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 1. 非控制器请求直接跳出
		if (!(handler instanceof HandlerMethod)) {
			return Boolean.TRUE;
		}
		// 2. 没有开启
		if (!xssProperties.isEnabled()) {
			return Boolean.TRUE;
		}
		// 3. 处理 XssIgnore 注解
		HandlerMethod handlerMethod = (HandlerMethod) handler;

		// 4.处理忽略类型
		XssCleanIgnore xssCleanIgnore = ClassUtil.getAnnotation(handlerMethod, XssCleanIgnore.class);
		if (null == xssCleanIgnore) {
			XssHolder.setEnable();
		}
		return Boolean.TRUE;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
		XssHolder.remove();
	}
}
