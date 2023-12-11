/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user;

import com.legendshop.common.core.util.NacosHostUtil;
import com.legendshop.common.feign.annotation.EnableLsFeignClients;
import com.legendshop.common.security.annotation.EnableResourceServer;
import com.legendshop.common.swagger.config.SwaggerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

/**
 * 用户服务启动类
 *
 * @author legendshop
 */
@Slf4j
@EnableLsFeignClients
@EnableResourceServer
@SpringBootApplication
@EnableDiscoveryClient
@Import({SwaggerConfig.class})
public class UserApplication {

	public static void main(String[] args) {
		NacosHostUtil.printNacos();
		SpringApplication.run(UserApplication.class, args);
		log.info("==========  UserApplication  系统启动成功! ==========");
	}

}
