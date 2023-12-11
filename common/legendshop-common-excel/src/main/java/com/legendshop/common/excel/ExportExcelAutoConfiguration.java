/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.excel;

import com.legendshop.common.excel.executor.ExportExcelExecutor;
import com.legendshop.common.excel.handler.ExportExcelReturnValueHandler;
import com.legendshop.common.excel.handler.SheetWriteHandler;
import com.legendshop.common.excel.processor.ExcelNameProcessor;
import com.legendshop.common.excel.processor.ExcelNameSpelExpressionProcessor;
import com.legendshop.common.excel.properties.ExcelProperties;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 导出excel自动化配置
 *
 * @author legendshop
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ExcelProperties.class)
@ComponentScan("com.legendshop.common.excel")
public class ExportExcelAutoConfiguration implements InitializingBean, ApplicationContextAware {

	private ApplicationContext applicationContext;
	private final List<SheetWriteHandler> sheetWriteHandlerList;


	@Override
	public void afterPropertiesSet() {
		RequestMappingHandlerAdapter handlerAdapter = applicationContext.getBean(RequestMappingHandlerAdapter.class);
		List<HandlerMethodReturnValueHandler> returnValueHandlers = handlerAdapter.getReturnValueHandlers();

		List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>();
		newHandlers.add(new ExportExcelReturnValueHandler((sheetWriteHandlerList)));
		assert returnValueHandlers != null;
		newHandlers.addAll(returnValueHandlers);
		handlerAdapter.setReturnValueHandlers(newHandlers);
	}

	@Bean
	@ConditionalOnMissingBean
	public ExcelNameProcessor nameProcessor() {
		return new ExcelNameSpelExpressionProcessor();
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Bean
	public ExportExcelExecutor exportExcelExecutor(AmqpSendMsgUtil amqpSendMsgUtil) {
		return new ExportExcelExecutor(amqpSendMsgUtil);
	}
}
