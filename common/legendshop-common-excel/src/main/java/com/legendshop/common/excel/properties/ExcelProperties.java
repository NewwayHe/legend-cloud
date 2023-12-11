/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.excel.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.legendshop.common.excel.properties.ExcelProperties.PREFIX;

/**
 * @author legendshop
 */
@Data
@ConfigurationProperties(prefix = PREFIX)
public class ExcelProperties {

	static final String PREFIX = "excel";

	/**
	 * 模板路径
	 */
	private String templatePath = "excel";
}
