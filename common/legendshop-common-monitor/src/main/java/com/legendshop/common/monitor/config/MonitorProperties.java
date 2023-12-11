/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.monitor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author legendshop
 * @date 2020-09-16 17:10
 **/
@Data
@ConfigurationProperties(prefix = "monitor")
public class MonitorProperties {

	/**
	 * 需要监控的服务
	 */
	private List<String> applications;
	/**
	 * 登录用户名
	 */
	private String loginUsername;
	/**
	 * 登录密码
	 */
	private String loginPassword;

}
