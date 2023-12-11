/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.sankuai.inf.leaf.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * @author legendshop
 */
public class PropertyFactory {
	private static final Logger logger = LoggerFactory.getLogger(PropertyFactory.class);
	private static final Properties prop = new Properties();

	static {
		try {
			prop.load(PropertyFactory.class.getClassLoader().getResourceAsStream("leaf.properties"));
		} catch (IOException e) {
			logger.warn("Load Properties Ex", e);
		}
	}

	public static Properties getProperties() {
		return prop;
	}
}
