/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.sankuai.inf.leaf.server.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置属性
 *
 * @author legendshop
 */
@Component("configProperties")
@Data
@ConfigurationProperties(prefix = "leaf")
public class ConfigProperties {

	String name;

	Segment segment;

	JdbcInfo jdbc;

	Snowflake snowflake;
}
