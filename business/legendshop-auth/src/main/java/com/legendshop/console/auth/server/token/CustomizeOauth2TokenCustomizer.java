/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.console.auth.server.token;

import com.legendshop.common.security.dto.BaseUserDetail;
import com.legendshop.console.auth.server.constant.Oauth2Constant;
import com.legendshop.console.auth.server.handler.UserInfoHandler;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;

/**
 * 自定义认证返回token信息增强
 *
 * @author legendshop
 */
public class CustomizeOauth2TokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {


	private Map<String, UserInfoHandler> userInfoHandler;

	public CustomizeOauth2TokenCustomizer(Map<String, UserInfoHandler> userInfoHandler) {
		this.userInfoHandler = userInfoHandler;
	}

	/**
	 * Customize the OAuth 2.0 Token attributes.
	 *
	 * @param context the context containing the OAuth 2.0 Token attributes
	 */
	@Override
	public void customize(OAuth2TokenClaimsContext context) {
		OAuth2TokenClaimsSet.Builder claims = context.getClaims();
		String clientId = context.getAuthorizationGrant().getName();
		claims.claim(Oauth2Constant.CLIENT_ID, clientId);
		// 客户端模式不返回具体用户信息
		if (Oauth2Constant.CLIENT_CREDENTIALS.equals(context.getAuthorizationGrantType().getValue())) {
			return;
		}

		// 此处应该根据 user_type，进行增强扩展
		BaseUserDetail baseUserDetail = (BaseUserDetail) context.getPrincipal().getPrincipal();
		Map<String, Object> objectMap = userInfoHandler.get(baseUserDetail.getUserType()).buildTokenEnhancer(baseUserDetail);
		if (!CollectionUtils.isEmpty(objectMap)) {
			Set<Map.Entry<String, Object>> entrySet = objectMap.entrySet();
			for (Map.Entry<String, Object> stringObjectEntry : entrySet) {
				claims.claim(stringObjectEntry.getKey(), stringObjectEntry.getValue());
			}
		}
	}

}
