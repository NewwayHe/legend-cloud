/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.security.config;

import com.legendshop.common.security.component.PermitAllUrlProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 资源服务器认证授权配置
 *
 * @author legendshop
 */
@RequiredArgsConstructor
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
@EnableWebSecurity
// Security debug模式
//@EnableWebSecurity(debug = true)
public class ResourceServerConfiguration {

	private final OpaqueTokenIntrospector customizeOpaqueTokenIntrospector;
	private final PermitAllUrlProperties permitAllUrl;

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
			throws Exception {

		// 放行URL
		AntPathRequestMatcher[] requestMatchers = permitAllUrl.getIgnoreUrls()
				.stream()
				.map(AntPathRequestMatcher::new)
				.toList()
				.toArray(new AntPathRequestMatcher[]{});
		httpSecurity.authorizeHttpRequests(authorize -> authorize.requestMatchers(requestMatchers).permitAll()
				//其他请求，需要认证
				.anyRequest().authenticated())
				.csrf(AbstractHttpConfigurer::disable)
				// 资源服务器向issuer-uri 发起 token 校验
				.oauth2ResourceServer(oauth2 -> oauth2
						.opaqueToken(opaqueTokenConfigurer -> opaqueTokenConfigurer.introspector(customizeOpaqueTokenIntrospector))
				)
				.sessionManagement(AbstractHttpConfigurer::disable);
		return httpSecurity.build();

	}
}
