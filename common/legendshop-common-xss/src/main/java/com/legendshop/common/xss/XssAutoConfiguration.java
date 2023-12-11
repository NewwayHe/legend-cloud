/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.xss;

import com.legendshop.common.xss.core.JacksonXssClean;
import com.legendshop.common.xss.core.XssCleanInterceptor;
import com.legendshop.common.xss.properties.XssProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * xss自动化配置类
 * 对from和json参数做xss
 *
 * @author legendshop
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(XssProperties.class)
@ConditionalOnProperty(value = "legendshop.xss.enabled", havingValue = "true", matchIfMissing = true)
public class XssAutoConfiguration implements WebMvcConfigurer {
	private final XssProperties xssProperties;


	@Bean
	public Jackson2ObjectMapperBuilderCustomizer xssJacksonCustomizer() {
		return builder -> builder.deserializerByType(String.class, new JacksonXssClean());
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		XssCleanInterceptor interceptor = new XssCleanInterceptor(xssProperties);
		registry.addInterceptor(interceptor)
				.addPathPatterns(xssProperties.getPathPatterns())
				.excludePathPatterns(xssProperties.getExcludePatterns())
				.order(Ordered.LOWEST_PRECEDENCE);
	}
}
