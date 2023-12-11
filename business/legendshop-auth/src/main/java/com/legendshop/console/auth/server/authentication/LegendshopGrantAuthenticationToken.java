/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.console.auth.server.authentication;

import com.legendshop.console.auth.server.constant.CustomizeOauth2ParameterNames;
import jakarta.annotation.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * 聚合登录自定义TOKEN
 *
 * @author legendshop
 */
public class LegendshopGrantAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken implements Serializable {

	@Serial
	private static final long serialVersionUID = 7641203583744560309L;

	public LegendshopGrantAuthenticationToken(Authentication clientPrincipal,
											  @Nullable Map<String, Object> additionalParameters) {
		super(new AuthorizationGrantType((String) additionalParameters.get(CustomizeOauth2ParameterNames.GRANT_TYPE)),
				clientPrincipal, additionalParameters);
	}
}
