/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.gateway;

import com.legendshop.common.core.util.NacosHostUtil;
import com.legendshop.common.gateway.annotation.EnableDynamicRoute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 网关应用
 *
 * @author legendshop
 */
@EnableDynamicRoute
@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class GatewayApplication {

	public static void main(String[] args) {
		NacosHostUtil.printNacos();
		SpringApplication.run(GatewayApplication.class, args);
		log.info("==========  GatewayApplication  系统启动成功! ==========");
	}

}
