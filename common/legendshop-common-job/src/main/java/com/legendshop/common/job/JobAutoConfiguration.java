/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.job;

import com.legendshop.common.job.properties.JobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.legendshop.common.job.properties.JobProperties.*;

/**
 * xxl-job的自动化配置类
 *
 * @author legendshop
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(JobProperties.class)
@Slf4j
public class JobAutoConfiguration {

	/**
	 * 初始化定时器
	 *
	 * @return
	 */
	@Bean
	@ConditionalOnProperty(prefix = PREFIX, value = "enabled", havingValue = "true", matchIfMissing = true)
	public XxlJobSpringExecutor xxlJobExecutor(JobProperties properties) {
		log.info(">>>>>>>>>>> xxl-job init config");
		XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
		// 获取 admin 管理端的地址
		XxlJobAdminProperties admin = properties.getAdmin();
		xxlJobSpringExecutor.setAdminAddresses(admin.getAddress());
		xxlJobSpringExecutor.setAccessToken(admin.getAccessToken());

		XxlJobExecutorProperties executor = properties.getExecutor();
		xxlJobSpringExecutor.setAppname(executor.getAppName());
		xxlJobSpringExecutor.setIp(executor.getIp());
		xxlJobSpringExecutor.setPort(executor.getPort());
		xxlJobSpringExecutor.setLogPath(executor.getLogPath());
		xxlJobSpringExecutor.setLogRetentionDays(executor.getLogRetentionDays());
		log.info(">>>>>>>>>>> xxl-job init success");
		return xxlJobSpringExecutor;
	}

}
