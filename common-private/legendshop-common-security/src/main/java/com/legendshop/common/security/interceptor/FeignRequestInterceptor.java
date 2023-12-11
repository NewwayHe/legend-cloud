/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.security.interceptor;

import com.legendshop.common.core.constant.RequestLogConstant;
import com.legendshop.common.core.context.RequestLogContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

/**
 * @author legendshop
 */
@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		requestTemplate.header(RequestLogConstant.TRACE_ID, RequestLogContext.getTraceId());
	}
}
