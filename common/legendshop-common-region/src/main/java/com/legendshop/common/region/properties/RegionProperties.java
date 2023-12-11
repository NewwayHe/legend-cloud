/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.region.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author legendshop
 */
@Data
@ConfigurationProperties(prefix = "legendshop.region")
public class RegionProperties {


	/**
	 * ip2region.db 文件路径
	 */
	private String dbPath = "classpath:db/region.db";
}
