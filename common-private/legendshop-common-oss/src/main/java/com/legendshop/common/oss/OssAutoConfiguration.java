/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.oss;

import com.legendshop.common.oss.endpoint.OssEndpoint;
import com.legendshop.common.oss.http.OssService;
import com.legendshop.common.oss.properties.OssProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * sws oss自动配置
 *
 * @author legendshop
 */
@AllArgsConstructor
@EnableConfigurationProperties(OssProperties.class)
public class OssAutoConfiguration {

	private final OssProperties properties;

	@Bean
	@ConditionalOnMissingBean(OssService.class)
	@ConditionalOnProperty(name = "legendshop.oss.enable", havingValue = "true", matchIfMissing = true)
	public OssService ossService() {
		return new OssService(properties);
	}

	@Bean
	@ConditionalOnProperty(name = "legendshop.oss.info", havingValue = "true")
	public OssEndpoint ossEndpoint(OssService ossService) {
		return new OssEndpoint(ossService);
	}

}
