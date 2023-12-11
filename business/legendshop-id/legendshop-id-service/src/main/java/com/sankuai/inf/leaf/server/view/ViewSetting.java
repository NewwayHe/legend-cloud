/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.sankuai.inf.leaf.server.view;

import com.sankuai.inf.leaf.server.properties.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

/**
 * @author legendshop
 */
@Component
public class ViewSetting {

	@Autowired
	ConfigProperties configProperties;

	@Bean
	FreeMarkerViewResolver freeMarkerViewResolver() {
		FreeMarkerViewResolver r = new FreeMarkerViewResolver();
		r.setPrefix("");
		r.setSuffix(".ftl");
		r.setOrder(-1);
		r.setCache(false);
		r.setContentType("text/html;charset=UTF-8");
		return r;
	}


}
