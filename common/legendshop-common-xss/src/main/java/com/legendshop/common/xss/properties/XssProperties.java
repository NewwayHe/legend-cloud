/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.xss.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author legendshop
 */
@ConfigurationProperties("legendshop.xss")
@Data
public class XssProperties {
	/**
	 * 开启xss
	 */
	private boolean enabled = true;
	/**
	 * 拦截的路由，默认为空
	 */
	private List<String> pathPatterns = new ArrayList<>();
	/**
	 * 放行的规则，默认为空
	 */
	private List<String> excludePatterns = new ArrayList<>();
}
