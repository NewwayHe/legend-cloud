/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;

import lombok.Getter;

/**
 * @author legendshop
 */
@Getter
public enum FrontEndPageEnum {
	/**
	 * 第三方登录回调页面 - ModulesUser/thirdLogin/thirdLoginResult
	 */
	AUTH_CALLBACK("ModulesUser/thirdLogin/thirdLoginResult", "第三方登录回调页面"),

	/**
	 * 第三方登录回调页面（PC端） - login/thirdAuth
	 */
	PC_AUTH_CALLBACK("login/thirdAuth", "第三方登录回调页面");

	private String url;

	private String desc;

	FrontEndPageEnum(String url, String desc) {
		this.url = url;
		this.desc = desc;
	}
}
