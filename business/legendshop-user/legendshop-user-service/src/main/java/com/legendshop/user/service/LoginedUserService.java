/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service;


import com.legendshop.user.dto.LoginedUserInfoDTO;

/**
 * 获取已经登录的用户信息, 手动获取用户信息看AppTokenUtil
 *
 * @author legendshop
 */
public interface LoginedUserService {

	/**
	 * 获取用户信息
	 */
	LoginedUserInfoDTO getUser();

}
