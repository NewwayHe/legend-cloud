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

/**
 * 账号的建立、更改、删除，账号的密码
 * 重置、锁定及激活，权限的修改
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum EventTypeEnum {

	/**
	 * 添加权限
	 */
	ADD_PREM("ADD_PREM", "添加权限"),

	/**
	 * 修改权限
	 */
	UPDATE_PREM("UPDATE_PREM", "修改权限"),

	/**
	 * 删除权限
	 */
	DEL_PREM("DEL_PREM", "删除权限"),

	/**
	 * 添加账号
	 */
	ADD_ACCOUNT("ADD_ACCOUNT", "添加账号"),

	/**
	 * 修改账号
	 */
	UPDATE_ACCOUNT("UPDATE_ACCOUNT", "修改账号"),

	/**
	 * 删除账号
	 */
	DEL_ACCOUNT("DEL_ACCOUNT", "删除账号"),

	/**
	 * 账号重置密码
	 */
	ACCOUNT_RESET_PASSWORD("ACCOUNT_RESET_PASSWORD", "账号重置密码"),

	/**
	 * 账号锁定
	 */
	ACCOUNT_LOCK("ACCOUNT_LOCK", "账号锁定"),

	/**
	 * 账号激活
	 */
	ACCOUNT_ACTIVATION("ACCOUNT_ACTIVATION", "账号激活"),

	;

	private final String eventType;

	private final String eventName;

	public static String getTypeName(String type) {
		for (EventTypeEnum eventTypeEnum : EventTypeEnum.values()) {
			if (eventTypeEnum.getEventType().equals(type)) {
				return eventTypeEnum.getEventName();
			}
		}
		return null;
	}
}
