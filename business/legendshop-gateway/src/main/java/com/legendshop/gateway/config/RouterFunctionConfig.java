/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.gateway.config;

import com.legendshop.gateway.handler.CodeCheckHandler;
import com.legendshop.gateway.handler.CodeGenerateHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 * 所有网关路由的配置
 *
 * @author legendshop
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class RouterFunctionConfig {

	private final CodeCheckHandler codeCheckHandler;

	private final CodeGenerateHandler codeGenerateHandler;


	@Bean
	public RouterFunction routerFunction() {
		return RouterFunctions
				.route(RequestPredicates.path("/code").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
						codeGenerateHandler)
				.andRoute(RequestPredicates.POST("/code/check").and(RequestPredicates.accept(MediaType.ALL)),
						codeCheckHandler);
	}

}
