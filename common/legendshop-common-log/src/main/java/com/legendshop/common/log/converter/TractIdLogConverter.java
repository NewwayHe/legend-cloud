/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.log.converter;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.legendshop.common.core.constant.RequestLogConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 由于多台服务器的日志都打印到同一个文件，为了区分日志来源，我们还得需要打印出日志信息对应的主机 ip 地址。
 * 获取之前访问的应用的名字
 *
 * @author legendshop
 */
@Slf4j
public class TractIdLogConverter extends ClassicConverter {

	@Override
	public String convert(ILoggingEvent event) {
		String traceId = "UNKNOW";

		try {

			HttpServletRequest request = this.getRequest();
			if (request != null) {
				traceId = request.getHeader(RequestLogConstant.TRACE_ID);
			}
			return traceId;

		} catch (Exception e) {
			log.error("", e);
		}
		return traceId;
	}

	protected HttpServletRequest getRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attributes == null ? null : attributes.getRequest();
	}
}
