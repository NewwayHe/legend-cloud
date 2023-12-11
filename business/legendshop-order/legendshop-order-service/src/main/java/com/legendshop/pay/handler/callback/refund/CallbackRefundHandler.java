/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.callback.refund;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author legendshop
 */
public interface CallbackRefundHandler {

	/**
	 * 处理方法入口
	 */
	String handler(HttpServletRequest request, HttpServletResponse response);


}
