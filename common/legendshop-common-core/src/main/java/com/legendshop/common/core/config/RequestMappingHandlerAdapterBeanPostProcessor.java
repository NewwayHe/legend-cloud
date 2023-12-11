/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.config;

import cn.hutool.core.collection.CollUtil;
import com.legendshop.common.core.config.resolver.CustomRequestParamMethodArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author legendshop
 * @version 1.0.0
 * @title RequestMappingHandlerAdapterBeanPostProcessor
 * @date 2022/4/6 17:56
 * @description：
 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
 */
@Slf4j
public class RequestMappingHandlerAdapterBeanPostProcessor implements BeanPostProcessor {

	private ConfigurableBeanFactory beanFactory;

	public RequestMappingHandlerAdapterBeanPostProcessor(ConfigurableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof RequestMappingHandlerAdapter) {
			log.info("请求映射处理适配器初始化后的后置处理开始~");
			List<HandlerMethodArgumentResolver> argumentResolvers = ((RequestMappingHandlerAdapter) bean).getArgumentResolvers();

			List<HandlerMethodArgumentResolver> newArgumentResolvers = new ArrayList<>();
			newArgumentResolvers.add(new CustomRequestParamMethodArgumentResolver(beanFactory, false));
			if (CollUtil.isNotEmpty(argumentResolvers)) {
				newArgumentResolvers.addAll(argumentResolvers);
			}

			HandlerMethodArgumentResolver handlerMethodArgumentResolver = newArgumentResolvers.get(newArgumentResolvers.size() - 2);
			if (handlerMethodArgumentResolver instanceof RequestMappingHandlerAdapter) {
				newArgumentResolvers.remove(newArgumentResolvers.size() - 2);
				newArgumentResolvers.add(newArgumentResolvers.size() - 1, new CustomRequestParamMethodArgumentResolver(beanFactory, true));
			}
			((RequestMappingHandlerAdapter) bean).setArgumentResolvers(newArgumentResolvers);
		}

		return bean;
	}
}
