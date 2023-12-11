/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录成功的实体
 *
 * @author legendshop
 */
@Data
@AllArgsConstructor
public class LoginSuccess implements Serializable {

	private static final long serialVersionUID = 2559811594936950714L;

	/**
	 * 用户Id
	 */
	private Long userId;

	/**
	 * 用户名称
	 */
	private String userName;

	/**
	 * IP地址
	 */
	private String ipAddress;

	/**
	 * 用户登录类型
	 */
	private String loginUserType;

	/**
	 * 用户登录来源
	 */
	private String loginSource;
}
