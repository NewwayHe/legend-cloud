/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.console.auth.server.endpoint;

import com.legendshop.console.auth.server.response.ResponseResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * 自定义异常处理
 *
 * @author legendshop
 */
public class CustomizeAuthenticationEntryPoint implements AuthenticationEntryPoint {


	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

		if (authException instanceof InsufficientAuthenticationException) {
			// 如果是api请求类型，则返回json
			ResponseResult.exceptionResponse(response, null, "需要带上令牌进行访问");

		} else if (authException instanceof InvalidBearerTokenException) {
			ResponseResult.exceptionResponse(response, null, "令牌无效或已过期");
		} else {
			ResponseResult.exceptionResponse(response, authException);
		}
	}
}
