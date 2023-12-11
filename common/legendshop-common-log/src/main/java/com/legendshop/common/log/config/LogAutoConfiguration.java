/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.log.config;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import com.legendshop.common.log.aop.LegendRequestLogAop;
import com.legendshop.common.log.aop.SystemLogAop;
import com.legendshop.common.log.event.IdSeqInitEvent;
import com.legendshop.common.log.listener.SystemLogListener;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 开启日志异步配置
 *
 * @author legendshop
 */
@Slf4j
@EnableAsync
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication
public class LogAutoConfiguration {

	//private final Environment environment;

	private final AmqpSendMsgUtil amqpSendMsgUtil;

	@Bean
	public SystemLogListener sysLogListener() {
		return new SystemLogListener(amqpSendMsgUtil);
	}

	@Bean
	public SystemLogAop sysLogAspect(ApplicationEventPublisher publisher) {
		return new SystemLogAop(publisher);
	}

	@Bean
	public LegendRequestLogAop legendRequestLogAop() {
		return new LegendRequestLogAop();
	}

	@Async
	@Order
	@SneakyThrows
	@EventListener({WebServerInitializedEvent.class, IdSeqInitEvent.class})
	public void initRoute() {

		IdGeneratorOptions options = new IdGeneratorOptions((short) 1);
		YitIdHelper.setIdGenerator(options);

		// 大规模集群需要使用Redis来控制 全局ID的获取，默认使用机器mac+pid+随机数生成

		// 获取服务名称
		//String applicationName = this.environment.getProperty("spring.application.name");
		//// 获取当前服务注册实例
		//List<ServiceInstance> instances = this.discoveryClient.getInstances(applicationName);

		//Integer serviceCount = this.redisTemplate.opsForValue().get("Legendshop_Service_Count");
		//if (null == serviceCount) {
		//	serviceCount = 0;
		//}
		//Integer integer = this.redisTemplate.opsForValue().get("Legendshop_Service_" + applicationName);
		//if (null == integer) {
		//	integer = 1;
		//	this.redisTemplate.opsForValue().set("Legendshop_Service_Count", serviceCount + 1);
		//} else {
		//	integer += 1;
		//}
		//this.redisTemplate.opsForValue().set("Legendshop_Service_" + applicationName, integer);

		//IdGenerateUtil.setInitSerial(serviceCount, integer);
	}
}
