/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.console.auth.server.handler;

import com.legendshop.common.security.dto.BaseUserDetail;
import com.legendshop.user.dto.BasisLoginParamsDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

/**
 * 用户信息接口封装
 *
 * @author legendshop
 */
public interface UserInfoHandler {

	/**
	 * 构建token扩展
	 *
	 * @param baseUserDetail the 需要拓展的token
	 * @return map
	 */
	Map<String, Object> buildTokenEnhancer(BaseUserDetail baseUserDetail);

	/**
	 * 构建spring security的UserDetails
	 *
	 * @param username the 用户信息
	 * @return UserDetails
	 */
	UserDetails buildUserDetails(String username) throws UsernameNotFoundException;

	/**
	 * *
	 *
	 * @param loginParams
	 * @return
	 * @throws UsernameNotFoundException
	 */
	UserDetails buildUserDetails(BasisLoginParamsDTO loginParams) throws UsernameNotFoundException;

	/**
	 * 锁定用户
	 *
	 * @param username
	 */
	void userLock(String username);
}
