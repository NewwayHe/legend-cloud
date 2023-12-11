/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.feign.expetion;

/**
 * 认证异常
 *
 * @author legendshop
 * @create: 2021-08-09 16:33
 */
public class FeignAuthException extends RuntimeException {

	private static final long serialVersionUID = 6299454644654020015L;

	public FeignAuthException(String msg) {
		super(msg);
	}

	public FeignAuthException(String msg, Throwable t) {
		super(msg, t);
	}
}
