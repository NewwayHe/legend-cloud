/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.console.auth.server.config;

import com.legendshop.console.auth.server.authentication.LegendshopAuthenticationProvider;
import com.legendshop.console.auth.server.authentication.LegendshopGrantAuthenticationConverter;
import com.legendshop.console.auth.server.endpoint.CustomizeAuthenticationEntryPoint;
import com.legendshop.console.auth.server.filter.CustomizeExceptionTranslationFilter;
import com.legendshop.console.auth.server.handler.CustomizeAccessDeniedHandler;
import com.legendshop.console.auth.server.handler.CustomizeAuthenticationFailHandler;
import com.legendshop.console.auth.server.handler.CustomizeAuthenticationSuccessHandler;
import com.legendshop.console.auth.server.handler.UserInfoHandler;
import com.legendshop.console.auth.server.token.CustomizeOauth2AccessTokenGenerator;
import com.legendshop.console.auth.server.token.CustomizeOauth2TokenCustomizer;
import com.legendshop.console.auth.server.utils.RequestLimiterUtil;
import com.legendshop.order.api.CartApi;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import java.util.Map;

/**
 * 认证服务配置中心
 *
 * @author legendshop
 */
@Configuration
@RequiredArgsConstructor
public class AuthorizationServerConfig {

	private final Map<String, UserInfoHandler> userInfoHandler;
	private final ApplicationEventPublisher publisher;
	private final CartApi cartApi;
	private final OAuth2AuthorizationService authorizationService;

	/**
	 * Spring Authorization Server 相关配置
	 * 此处方法与下面defaultSecurityFilterChain都是SecurityFilterChain配置，配置的内容有点区别，
	 * 因为Spring Authorization Server是建立在Spring Security 基础上的，defaultSecurityFilterChain方法主要
	 * 配置Spring Security相关的东西，而此处authorizationServerSecurityFilterChain方法主要配置OAuth 2.1和OpenID Connect 1.0相关的东西
	 */
	@Bean
	@Order(1)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity httpSecurity,
																	  OAuth2TokenGenerator<?> tokenGenerator,
																	  RequestLimiterUtil limiterUtil

	) throws Exception {

		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);

		httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
				// Legendshop 聚合登录统一处理
				.tokenEndpoint(tokenEndpoint ->
						tokenEndpoint
								// 认证参数转换类（认证入口）
								.accessTokenRequestConverter(
										new LegendshopGrantAuthenticationConverter())
								// 认证核心处理
								.authenticationProvider(
										new LegendshopAuthenticationProvider(
												authorizationService, tokenGenerator, limiterUtil))
								// 认证失败后处理
								.errorResponseHandler(new CustomizeAuthenticationFailHandler())
								// 认证成功后处理
								.accessTokenResponseHandler(new CustomizeAuthenticationSuccessHandler(publisher, cartApi))

				)
				// 客户端认证失败处理
				.clientAuthentication(clientAuthentication -> {
					clientAuthentication.errorResponseHandler(new CustomizeAuthenticationFailHandler());
				})
				// 开启OpenID Connect 1.0（其中oidc为OpenID Connect的缩写）。
				.oidc(Customizer.withDefaults());

		httpSecurity
				// 异常处理
				.exceptionHandling((exceptions) -> exceptions
						.authenticationEntryPoint(new CustomizeAuthenticationEntryPoint())
						.accessDeniedHandler(new CustomizeAccessDeniedHandler())
				)
				// 前置过滤
				.addFilterBefore(new CustomizeExceptionTranslationFilter(), ExceptionTranslationFilter.class);

		return httpSecurity.build();
	}


	/**
	 * 客户端信息
	 * 对应表：oauth2_registered_client
	 */
	@Bean
	public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
		return new JdbcRegisteredClientRepository(jdbcTemplate);
	}

	/**
	 * 授权确认
	 * 对应表：oauth2_authorization_consent
	 */
	@Bean
	public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
		return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 配置认证服务器请求地址
	 */
	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		// 什么都不配置，则使用默认地址
		return AuthorizationServerSettings.builder().build();
	}

	/**
	 * 配置token生成器以及token增强
	 */
	@Bean
	OAuth2TokenGenerator<?> tokenGenerator() {

		CustomizeOauth2AccessTokenGenerator accessTokenGenerator = new CustomizeOauth2AccessTokenGenerator();
		OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();

		// 注入Token 增加关联用户信息
		accessTokenGenerator.setAccessTokenCustomizer(new CustomizeOauth2TokenCustomizer(userInfoHandler));
		return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, refreshTokenGenerator);
	}
}
