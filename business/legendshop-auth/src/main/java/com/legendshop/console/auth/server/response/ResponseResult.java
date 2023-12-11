/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.console.auth.server.response;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.exception.NotBindUserException;
import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author legendshop
 */
@Data
@Slf4j
public class ResponseResult<T> {

	/**
	 * 认证服务异常响应
	 */
	public static void exceptionResponse(HttpServletResponse response, Exception e) throws AccessDeniedException, AuthenticationException, IOException {

		String message;
		Integer code;

		if (e instanceof OAuth2AuthenticationException o) {
			message = o.getError().getDescription();
		} else {
			message = e.getMessage();
		}
		if (e instanceof NotBindUserException) {
			code = ((NotBindUserException) e).getHttpErrorCode();
		} else {
			code = ResultCodeEnum.FAIL.getCode();
		}
		exceptionResponse(response, code, message);
	}

	/**
	 * 认证服务异常响应
	 */
	public static void exceptionResponse(HttpServletResponse response, Integer code, String message) throws AccessDeniedException, AuthenticationException, IOException {

		log.error("登录认证失败：{}", message);
		if (ObjectUtils.isEmpty(code)) {
			code = ResultCodeEnum.FAIL.getCode();
		}
		// 返回系统统一结果集
		R<String> responseResult = R.fail(code, message, message);
		Gson gson = new Gson();
		String jsonResult = gson.toJson(responseResult);
		log.error("登录认证失败返回信息：{}", jsonResult);

		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		if (HttpStatus.CREATED.value() == code) {
			response.setStatus(code);
		}

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.getWriter().print(jsonResult);
	}

}
