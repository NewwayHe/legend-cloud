/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.console.auth.server.authentication;

import com.legendshop.common.security.enums.LegendshopSecurityErrorEnum;
import com.legendshop.console.auth.server.constant.Oauth2Constant;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

/**
 * 聚合登录类型转换自定义Token
 *
 * @author legendshop
 */
public class LegendshopGrantAuthenticationConverter implements AuthenticationConverter {


	@Nullable
	@Override
	public Authentication convert(HttpServletRequest request) {

		// grant_type (REQUIRED) TODO ... 为实现用户端可能防止同时在线，会有不同的端
		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
		if (!Oauth2Constant.GRANT_TYPE_LEGENDSHOP.equals(grantType) && !Oauth2Constant.GRANT_TYPE_REFRESH_TOKEN.equals(grantType)) {
			throw LegendshopSecurityErrorEnum.ERROR_AUTH_TYPE.getException();
		}

		Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

		//从request中提取请求参数，然后存入MultiValueMap<String, String>
		MultiValueMap<String, String> parameters = getParameters(request);

		Map<String, Object> additionalParameters = new HashMap<>(16);
		//遍历从request中提取的参数，排除掉code等字段参数，其他参数收集到additionalParameters中
		parameters.forEach((key, value) -> {
			if (!key.equals(OAuth2ParameterNames.CODE)) {
				additionalParameters.put(key, value.get(0));
			}
		});
		//返回自定义的LegendshopGrantAuthenticationToken对象
		return new LegendshopGrantAuthenticationToken(clientPrincipal, additionalParameters);
	}

	/**
	 * 从request中提取请求参数，然后存入MultiValueMap<String, String>
	 */
	private static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
		parameterMap.forEach((key, values) -> {
			if (values.length > 0) {
				for (String value : values) {
					parameters.add(key, value);
				}
			}
		});
		return parameters;
	}

}


