/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.console.auth.server;


import com.legendshop.common.core.util.NacosHostUtil;
import com.legendshop.common.feign.annotation.EnableLsFeignClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 认证服务启动类
 *
 * @author legendshop
 */
@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
@EnableLsFeignClients
public class AuthApplication {
	public static void main(String[] args) {
		NacosHostUtil.printNacos();
		SpringApplication.run(AuthApplication.class, args);
		log.info("==========  AuthApplication  系统启动成功! ==========");
	}
}
