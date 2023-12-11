/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.region;

import com.legendshop.common.region.properties.RegionProperties;
import com.legendshop.common.region.service.RegionSearcherService;
import com.legendshop.common.region.service.impl.RegionSearcherImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

/**
 * 根据ip获取地区的自动化配置类
 *
 * @author legendshop
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RegionProperties.class)
public class RegionAutoConfiguration {

	/**
	 * 注入spring Bean
	 *
	 * @param resourceLoader
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public RegionSearcherService regionSearcher(ResourceLoader resourceLoader, RegionProperties regionProperties) {
		return new RegionSearcherImpl(resourceLoader, regionProperties);
	}
}
