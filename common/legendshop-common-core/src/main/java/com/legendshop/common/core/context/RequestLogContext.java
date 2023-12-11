/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.context;

/**
 * @author legendshop
 */
public class RequestLogContext {

	private final static ThreadLocal<String> TRACE_ID_THREAD_LOCAL = new ThreadLocal<>();

	public static void addTraceId(String id) {
		TRACE_ID_THREAD_LOCAL.set(id);
	}

	public static String getTraceId() {
		return TRACE_ID_THREAD_LOCAL.get();
	}

	public static void removeTraceId() {
		TRACE_ID_THREAD_LOCAL.remove();
	}
}
