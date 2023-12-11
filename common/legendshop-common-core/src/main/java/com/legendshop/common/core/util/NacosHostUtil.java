/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.util;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Nacos启动的时候注明是使用哪个Nacos
 *
 * @author legendshop
 */
@Slf4j
public class NacosHostUtil {

	/**
	 * 跟gitlab的标签保持一致，每次上正式环境都要更改这个字段，并打印到日志里，用于分辨当前的系统的版本号，判断当前系统的升级情况
	 */
	public static String PROD_VERSION = "V7.0";

	public static void printNacos() {
		String hostName = System.getProperty("NACOS_HOST");


		if (hostName != null) {
			log.info("System Nacos Host {}", hostName);
		}
		String port = System.getProperty("NACOS_PORT");
		if (port != null) {
			log.info("System Nacos Port {}", port);
		}

		String namespace = System.getProperty("NACOS_NAMESPACE");
		if (StrUtil.isNotEmpty(namespace)) {
			log.info("System Nacos Namespace {}", namespace);
		}

		log.info("当前系统的版本号: {}", PROD_VERSION);
	}
}
