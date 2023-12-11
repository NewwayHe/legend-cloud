/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.enums;


import com.legendshop.common.core.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色类型
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum RoleTypeEnum implements BaseEnum<String> {

	/**
	 * admin管理员角色
	 */
	ADMIN("A"),

	/**
	 * shop商家角色
	 */
	SHOP("S"),

	/**
	 * shop商家子用户
	 */
	SHOP_SUB_USER("SSU"),

	/**
	 * user买家用户角色
	 */
	USER("U");

	private final String value;

	@Override
	public boolean contains(String value) {
		for (RoleTypeEnum typeEnum : RoleTypeEnum.values()) {
			if (typeEnum.name().equals(value)) {
				return true;
			}
		}
		return false;
	}
}
