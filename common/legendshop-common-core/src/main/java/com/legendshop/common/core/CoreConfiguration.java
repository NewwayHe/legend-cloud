/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core;

import com.legendshop.common.core.config.RequestMappingHandlerAdapterBeanPostProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author legendshop
 * @version 1.0.0
 * @title CoreConfigure
 * @date 2022/4/7 11:49
 * @description：
 */
@Configuration
public class CoreConfiguration implements BeanFactoryAware {

	private ConfigurableBeanFactory beanFactory;

	/**
	 * 排除网关，如果网关启动会报错
	 *
	 * @return
	 */
	@Bean
	@ConditionalOnMissingClass(value = "com.legendshop.gateway.GatewayApplication")
	public RequestMappingHandlerAdapterBeanPostProcessor requestMappingHandlerAdapterBeanPostProcessor() {
		return new RequestMappingHandlerAdapterBeanPostProcessor(beanFactory);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		if (beanFactory instanceof ConfigurableBeanFactory) {
			this.beanFactory = (ConfigurableBeanFactory) beanFactory;
		}
	}

//	@Bean
//	public ThreadPoolExecutor threadPoolExecutor() {
//		return new ThreadPoolExecutor(2, 5, 1L, TimeUnit.MINUTES, new ArrayBlockingQueue(1000));
//	}

//	@InitBinder
//	public void initBinder(WebDataBinder binder) {
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		dateFormat.setLenient(false);
//		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
//	}
}
