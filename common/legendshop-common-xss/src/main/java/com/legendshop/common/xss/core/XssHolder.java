/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.xss.core;

class XssHolder {

	private static final ThreadLocal<Boolean> THREAD_LOCAL = new ThreadLocal<>();

	public static boolean isEnabled() {
		return Boolean.TRUE.equals(THREAD_LOCAL.get());
	}

	public static void setEnable() {
		THREAD_LOCAL.set(Boolean.TRUE);
	}

	public static void remove() {
		THREAD_LOCAL.remove();
	}
}
