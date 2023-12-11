/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.legendshop.common.core.constant.CacheConstants.*;

/**
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnum {

	/**
	 * admin管理员登录
	 */
	ADMIN("ADMIN", ADMIN_USER_DETAILS),

	/**
	 * shop商家登录
	 */
	SHOP("SHOP", SHOP_USER_DETAILS),

	/**
	 * 商家子账号
	 */
	SHOP_SUB_USER("SHOP_SUB_USER", SHOP_SUB_USER_DETAILS),

	/**
	 * 门店登录
	 */
	STORE("STORE", ""),

	/**
	 * user普通用户登录
	 */
	USER("USER", ORDINARY_USER_DETAILS),

	/**
	 * 用户社交登录
	 */
	USER_SOCIAL("USER_SOCIAL", "");

	private final String loginType;

	private final String cacheName;

	/**
	 * 是否存在登录类型
	 *
	 * @param loginType the loginType
	 * @return boolean
	 */
	public static boolean contains(String loginType) {
		for (UserTypeEnum typeEnum : UserTypeEnum.values()) {
			if (typeEnum.getLoginType().equals(loginType)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 转换枚举
	 *
	 * @param loginType the loginType
	 * @return String the cacheName
	 */
	public static String obtainCacheName(String loginType) {
		for (UserTypeEnum userTypeEnum : UserTypeEnum.values()) {
			if (userTypeEnum.getLoginType().equals(loginType)) {
				return userTypeEnum.getCacheName();
			}
		}
		return null;
	}

	public static UserTypeEnum codeValue(String name) {
		for (UserTypeEnum value : UserTypeEnum.values()) {
			if (value.name().equals(name)) {
				return value;
			}
		}
		throw new RuntimeException("错误的用户类型！");
	}

}
