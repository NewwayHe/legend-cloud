/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.sankuai.inf.leaf.snowflake.exception;

/**
 * @author legendshop
 */
public class ClockGoBackException extends RuntimeException {
	public ClockGoBackException(String message) {
		super(message);
	}
}