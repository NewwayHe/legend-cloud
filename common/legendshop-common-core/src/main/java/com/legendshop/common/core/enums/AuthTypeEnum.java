/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.enums;

/**
 * @author legendshop
 */
public enum AuthTypeEnum {

	PASSWORD("PASSWORD", ""),
	SMS("SMS", ""),
	WECHAT_PC("WECHAT_PC", ""),
	WECHAT_MP("WECHAT_MP", ""),
	WECHAT_MINI("WECHAT_MINI", ""),
	WECHAT_APP("WECHAT_APP", ""),
	QQ("QQ", ""),

	;

	private final String authType;

	private final String cacheName;

	AuthTypeEnum(String authType, String cacheName) {
		this.authType = authType;
		this.cacheName = cacheName;
	}

	public String authType() {
		return authType;
	}

	public String cacheName() {
		return cacheName;
	}

	public static AuthTypeEnum codeValue(String name) {
		for (AuthTypeEnum value : AuthTypeEnum.values()) {
			if (value.name().equals(name)) {
				return value;
			}
		}
		throw new RuntimeException("错误的认证方式！");
	}


}
