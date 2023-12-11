/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.console.auth.server.service;

/**
 * OAuth2 Access Token 处理
 *
 * @author legendshop
 */
public interface TokenHandleService {

	/**
	 * 删除Access Token
	 *
	 * @param token
	 * @param userType
	 * @return
	 */
	Boolean delToken(String token, String userType);
}
