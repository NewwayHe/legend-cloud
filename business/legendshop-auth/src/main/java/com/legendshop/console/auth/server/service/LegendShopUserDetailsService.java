/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.console.auth.server.service;

import com.legendshop.console.auth.server.authentication.LegendshopGrantAuthenticationToken;
import com.legendshop.user.dto.BasisLoginParamsDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 用户登录的service
 *
 * @author legendshop
 */
public interface LegendShopUserDetailsService extends UserDetailsService {

	/**
	 * 根据用户类型获取用户
	 *
	 * @param loginParams the 用户登录信息
	 * @return userDetails the 用户信息
	 */
	UserDetails loadUserByUsername(BasisLoginParamsDTO loginParams) throws UsernameNotFoundException;

	/**
	 * 锁定账户
	 *
	 * @param authenticationToken
	 */
	void userLock(LegendshopGrantAuthenticationToken authenticationToken);
}
