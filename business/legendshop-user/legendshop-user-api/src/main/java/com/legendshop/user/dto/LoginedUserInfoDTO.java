/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 已经登录的用户详细信息,使用者不能修改
 *
 * @author legendshop
 */
@Data
public class LoginedUserInfoDTO implements Serializable {

	private static final long serialVersionUID = 3908632520035702509L;

	/**
	 * 用户Id
	 */
	private final Long userId;

	/**
	 * 商家Id
	 */
	private final Long shopId;

	/**
	 * openId
	 */
	private final String accessToken;

	/**
	 * 微信用户openId
	 */
	private String openId;

}
