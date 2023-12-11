/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.console.auth.server.constant;

/**
 * 认证服务常量
 *
 * @author legendshop
 */
public class Oauth2Constant {

	/**
	 * 登录最高重试次数
	 */
	public final static Integer MAX_COUNT = 20;

	/**
	 * refresh_token
	 */
	public static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";

	/**
	 * legendshop聚合登录类型（自定义）
	 */
	public static final String GRANT_TYPE_LEGENDSHOP = "legendshop";

	/**
	 * 客户端模式
	 */
	public static final String CLIENT_CREDENTIALS = "client_credentials";

	/**
	 * 客户端ID
	 */
	public static final String CLIENT_ID = "clientId";


	/**
	 * 构造方法私有化
	 */
	private Oauth2Constant() {

	}
}
