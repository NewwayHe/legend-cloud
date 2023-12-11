/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.swagger.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author legendshop
 */
@Configuration
@OpenAPIDefinition(info = @Info(
		title = "dev7.0微服务接口文档",
		version = "7.0",
		description = "Spring shop sample application",
		contact = @Contact(
				name = "Your Name",
				email = "your.email@example.com"
		)
))
public class SwaggerConfig {
	@Bean
	public GroupedOpenApi adminApi() {
		return GroupedOpenApi.builder()
				.group("Default")
				.pathsToMatch("/**")
				.packagesToExclude("/rpc-api/**")
				.addOpenApiMethodFilter(method -> method.isAnnotationPresent(Operation.class))
				.build();
	}


//	@Bean
//	public OpenAPI springShopOpenAPI() {
//		return new OpenAPI()
//				.info(new Info().title("dev7.0微服务接口文档")
//						.description("Spring shop sample application")
//						.version("v0.0.1")
//						.license(new License().name("Apache 2.0").url("http://springdoc.org")))
//				.externalDocs(new ExternalDocumentation()
//						.description("SpringShop Wiki Documentation")
//						.url("https://springshop.wiki.github.org/docs"));
//	}

}
