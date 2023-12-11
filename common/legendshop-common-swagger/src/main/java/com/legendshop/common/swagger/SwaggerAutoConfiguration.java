/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.swagger;
//
////import com.google.common.base.Predicate;
////import com.google.common.base.Predicates;
//
//import com.legendshop.common.swagger.properties.SwaggerProperties;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.Arrays;
//import java.util.List;
///**
//// * swagger自动化配置类
//// *
//// * @author legendshop
//// */
//public class SwaggerAutoConfiguration {
//
//	@Bean
//	@ConditionalOnMissingBean
//	public SwaggerProperties swaggerProperties() {
//		return new SwaggerProperties();
//	}
//
//	private static final String BASE_PATH = "/**";
//
//	/**
//	 * 默认的排除路径，排除Spring Boot默认的错误处理路径和端点
//	 */
//	private static final List<String> DEFAULT_EXCLUDE_PATH = Arrays.asList("/error", "/actuator/**");
//
////	@Bean
////	public Docket docket(SwaggerProperties swaggerProperties) {
////		if (swaggerProperties.getBasePath().isEmpty()) {
////			swaggerProperties.getBasePath().add(BASE_PATH);
////		}
////		List<Predicate<String>> basePath = new ArrayList();
////		swaggerProperties.getBasePath().forEach(path -> basePath.add(PathSelectors.ant(path)));
////
////		// exclude-path处理
////		if (swaggerProperties.getExcludePath().isEmpty()) {
////			swaggerProperties.getExcludePath().addAll(DEFAULT_EXCLUDE_PATH);
////		}
////		List<Predicate<String>> excludePath = new ArrayList<>();
////		swaggerProperties.getExcludePath().forEach(path -> excludePath.add(PathSelectors.ant(path)));
////
////
////
////		return new Docket(DocumentationType.SWAGGER_2).host(swaggerProperties.getHost())
////				.apiInfo(apiInfo(swaggerProperties)).select()
////				//只扫描添加了Api注解的类
////				.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
////				//只扫描添加了ApiOperation注解的方法
////				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//////				.apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
//////				.paths(Predicates.and(Predicates.not(Predicates.or(excludePath)), Predicates.or(basePath))).build()
////				.paths(PathSelectors.ant("/**")).build()
////				.pathMapping("/");
////	}
//
//
////	/**
////	 * api info对象
////	 *
////	 * @param swaggerProperties
////	 * @return
////	 */
////	private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
////		return new ApiInfoBuilder().title(swaggerProperties.getTitle()).description(swaggerProperties.getDescription())
////				.license(swaggerProperties.getLicense()).licenseUrl(swaggerProperties.getLicenseUrl())
////				.termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
////				.contact(new Contact(swaggerProperties.getContact().getName(), swaggerProperties.getContact().getUrl(),
////						swaggerProperties.getContact().getEmail()))
////				.version(swaggerProperties.getVersion()).build();
////	}
//
////	@Bean
////	public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
////		return new BeanPostProcessor() {
////
////			@Override
////			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
////				if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
////					customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
////				}
////				return bean;
////			}
////
////			private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
////				List<T> copy = mappings.stream()
////						.filter(mapping -> mapping.getPatternParser() == null)
////						.collect(Collectors.toList());
////				mappings.clear();
////				mappings.addAll(copy);
////			}
////
////			@SuppressWarnings("unchecked")
////			private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
////				try {
////					Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
////					field.setAccessible(true);
////					return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
////				} catch (IllegalArgumentException | IllegalAccessException e) {
////					throw new IllegalStateException(e);
////				}
////			}
////		};
////	}
//
//}
