/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop;

import com.legendshop.common.core.util.NacosHostUtil;
import com.legendshop.common.feign.annotation.EnableLsFeignClients;
import com.legendshop.common.security.annotation.EnableResourceServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 订单服务启动类
 *
 * @author legendshop
 */
@Slf4j
@EnableResourceServer
@EnableLsFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class OrderApplication {

	public static void main(String[] args) {
		NacosHostUtil.printNacos();
		SpringApplication.run(OrderApplication.class, args);
		log.info("==========  OrderApplication  系统启动成功! ==========");
	}

}
