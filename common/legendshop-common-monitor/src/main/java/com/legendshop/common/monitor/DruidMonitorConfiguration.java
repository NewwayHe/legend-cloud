/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.monitor;

import cn.hutool.core.util.StrUtil;
import com.legendshop.common.monitor.config.MonitorProperties;
import com.legendshop.common.monitor.servlet.MonitorViewServlet;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * druid自动化配置
 *
 * @author legendshop
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({MonitorProperties.class})
public class DruidMonitorConfiguration {
	public DruidMonitorConfiguration() {
	}

	@Bean
	public ServletRegistrationBean<MonitorViewServlet> statViewServletRegistrationBean(MonitorProperties properties) {
		ServletRegistrationBean<MonitorViewServlet> registrationBean = new ServletRegistrationBean<>();
		registrationBean.setServlet(new MonitorViewServlet());
		registrationBean.addUrlMappings("/druid/*");
		if (StrUtil.isNotBlank(properties.getLoginUsername())) {
			registrationBean.addInitParameter("loginUsername", properties.getLoginUsername());
		}

		if (StrUtil.isNotBlank(properties.getLoginPassword())) {
			registrationBean.addInitParameter("loginPassword", properties.getLoginPassword());
		}

		return registrationBean;
	}
}
