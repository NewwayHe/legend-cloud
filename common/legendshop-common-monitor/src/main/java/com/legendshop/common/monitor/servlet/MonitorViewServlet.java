/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.monitor.servlet;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author legendshop
 * @date 2020-09-16 11:10
 **/
@Slf4j
public class MonitorViewServlet implements Servlet {
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {

	}

	@Override
	public ServletConfig getServletConfig() {
		return null;
	}

	@Override
	public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {

	}

	@Override
	public String getServletInfo() {
		return null;
	}

	@Override
	public void destroy() {

	}

//	private MonitorStatService monitorStatService;
//
//	public MonitorViewServlet() {
//		super("support/http/resources");
//	}
//
//
//	public void init() {
//		log.info("init MonitorViewServlet");
//		init();
//		monitorStatService = SpringContextHolder.getBean(MonitorStatService.class);
//	}
//
//	@Override
//	protected String process(String url) {
//		return monitorStatService.service(url);
//	}

}
