/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.console.auth.server.service.impl;


import com.legendshop.common.security.utils.CryptographicSignatureUtil;
import com.legendshop.console.auth.server.authentication.LegendshopGrantAuthenticationToken;
import com.legendshop.console.auth.server.constant.CustomizeOauth2ParameterNames;
import com.legendshop.console.auth.server.handler.UserInfoHandler;
import com.legendshop.console.auth.server.service.LegendShopUserDetailsService;
import com.legendshop.user.dto.BasisLoginParamsDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 读取数据库配置
 *
 * @author legendshop
 */
@Service
@AllArgsConstructor
public class LegendShopUserDetailsServiceImpl implements LegendShopUserDetailsService {

	private final Map<String, UserInfoHandler> userInfoHandler;


	@Override
	public UserDetails loadUserByUsername(BasisLoginParamsDTO loginParams) throws UsernameNotFoundException {
		return this.userInfoHandler.get(loginParams.getUserType().getLoginType()).buildUserDetails(loginParams);
	}

	/**
	 * *
	 *
	 * @param username
	 * @return
	 * @throws UsernameNotFoundException
	 */
	@Deprecated
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return null;
	}

	@Override
	public void userLock(LegendshopGrantAuthenticationToken authenticationToken) {
		// 解密
		String principal = CryptographicSignatureUtil.decrypt((String) authenticationToken.getPrincipal());
		this.userInfoHandler.get(authenticationToken.getAdditionalParameters().get(CustomizeOauth2ParameterNames.USER_TYPE)).userLock(principal);
	}
}
