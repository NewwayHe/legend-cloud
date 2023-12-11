/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.security.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 授权客户端
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum ClientIdEnum {

	/**
	 * 后台管理员
	 */
	ADMIN("admin"),

	/**
	 * 商家
	 */
	SHOP("shop"),

	/**
	 * 用户PC端
	 */
	USER("user"),


	/**
	 * 用户移动端
	 */
	APP("app"),

	/**
	 * 移动h5
	 */
	H5("h5"),

	/**
	 * 公众号
	 */
	MP("mp"),

	/**
	 * 小程序
	 */
	MINI("mini"),
	;


	final String type;
}
