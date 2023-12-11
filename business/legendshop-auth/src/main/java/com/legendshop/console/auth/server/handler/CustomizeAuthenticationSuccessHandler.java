/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.console.auth.server.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.common.core.dto.SystemLogDTO;
import com.legendshop.common.log.event.SystemLogEvent;
import com.legendshop.common.log.util.SystemLogUtil;
import com.legendshop.common.security.dto.AdminUserDetail;
import com.legendshop.common.security.dto.BaseUserDetail;
import com.legendshop.common.security.dto.OrdinaryUserDetail;
import com.legendshop.common.security.dto.ShopUserDetail;
import com.legendshop.console.auth.server.authentication.LegendshopOAuth2AccessTokenResponseHttpMessageConverter;
import com.legendshop.console.auth.server.constant.CustomizeOauth2ParameterNames;
import com.legendshop.order.api.CartApi;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * 自定义处理登录成功的handler，保存日志
 *
 * @author legendshop
 */
@Component
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter = new LegendshopOAuth2AccessTokenResponseHttpMessageConverter();

	private CartApi cartApi;
	private ApplicationEventPublisher publisher;

	public CustomizeAuthenticationSuccessHandler(ApplicationEventPublisher applicationEventPublisher, CartApi cartApi) {
		this.publisher = applicationEventPublisher;
		this.cartApi = cartApi;
	}


	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

		// 登录成功后操作 TODO....
		successHandle(authentication, request, response);

		// 输出token
		sendAccessTokenResponse(request, response, authentication);
	}

	private void sendAccessTokenResponse(HttpServletRequest request, HttpServletResponse response,
										 Authentication authentication) throws IOException {

		OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;

		OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
		OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
		Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();

		OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
				.tokenType(accessToken.getTokenType())
				.scopes(accessToken.getScopes());
		if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
			builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
		}
		if (refreshToken != null) {
			builder.refreshToken(refreshToken.getTokenValue());
		}
		if (!CollectionUtils.isEmpty(additionalParameters)) {
			builder.additionalParameters(additionalParameters);
		}
		OAuth2AccessTokenResponse accessTokenResponse = builder.build();
		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

		// 无状态 注意删除 context 上下文的信息
		SecurityContextHolder.clearContext();
		this.accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);
	}


	private void successHandle(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {

		Object principal = authentication.getDetails();
		//普通用户登录成功后，触发绑定
		if (isOrdinaryUserAuthentication(authentication)) {

			OAuth2AccessTokenAuthenticationToken authenticationToken = (OAuth2AccessTokenAuthenticationToken) authentication;
			Map<String, Object> additionalParameters = authenticationToken.getAdditionalParameters();
			if (!CollectionUtils.isEmpty(additionalParameters)) {

				String visitorId = (String) additionalParameters.get(CustomizeOauth2ParameterNames.USER_KEY);
				if (StrUtil.isNotEmpty(visitorId)) {
					// TODO...改成异步，解耦依赖order&activity两个服务
				}
			}
		}
		BaseUserDetail baseUserDetail = (BaseUserDetail) principal;
		SystemLogDTO sysLog = SystemLogUtil.build(request, (BaseUserDetail) principal);
		sysLog.setTitle(userType(authentication));
		sysLog.setParams(baseUserDetail.getUsername());
		sysLog.setRequestUser(baseUserDetail.getUsername());
		sysLog.setCode(CommonConstants.SUCCESS);
		publisher.publishEvent(new SystemLogEvent(sysLog));
	}

	private String userType(Authentication authentication) {
		if (isAdminUserAuthentication(authentication)) {
			return "后台管理员";
		}

		if (isShopUserAuthentication(authentication)) {
			return "商家登录";
		}

		if (isOrdinaryUserAuthentication(authentication)) {
			return "用户登录";
		}
		return "";
	}

	/**
	 * 后台用户
	 */
	private boolean isAdminUserAuthentication(Authentication authentication) {
		return authentication.getPrincipal() instanceof AdminUserDetail
				&& CollUtil.isNotEmpty(authentication.getAuthorities());
	}

	/**
	 * 后台用户
	 */
	private boolean isShopUserAuthentication(Authentication authentication) {
		return authentication.getPrincipal() instanceof ShopUserDetail
				&& CollUtil.isNotEmpty(authentication.getAuthorities());
	}

	/**
	 * 普通用户
	 */
	private boolean isOrdinaryUserAuthentication(Authentication authentication) {
		return authentication.getDetails() instanceof OrdinaryUserDetail;
	}


}
