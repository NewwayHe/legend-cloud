/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.common.security.component.PermitAllUrlProperties;
import com.legendshop.common.security.component.ResourceAuthExceptionEntryPoint;
import com.legendshop.common.security.interceptor.CustomizeOpaqueTokenIntrospector;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

/**
 * 资源服务器自动化配置
 *
 * @author legendshop
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(PermitAllUrlProperties.class)
public class ResourceServerAutoConfiguration {

	/**
	 * 资源服务器token内省处理器
	 *
	 * @param authorizationService token 存储实现
	 * @return TokenIntrospector
	 */
	@Bean
	public OpaqueTokenIntrospector opaqueTokenIntrospector(OAuth2AuthorizationService authorizationService) {
		return new CustomizeOpaqueTokenIntrospector(authorizationService);
	}

	/**
	 * 资源服务器异常处理
	 * @param objectMapper jackson 输出对象
	 * @return ResourceAuthExceptionEntryPoint
	 */
	@Bean
	public ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint(ObjectMapper objectMapper) {
		return new ResourceAuthExceptionEntryPoint(objectMapper);
	}
}
