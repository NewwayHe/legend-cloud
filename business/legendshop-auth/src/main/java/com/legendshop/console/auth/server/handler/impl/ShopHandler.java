/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.console.auth.server.handler.impl;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.SecurityConstants;
import com.legendshop.common.core.enums.UserTypeEnum;
import com.legendshop.common.security.dto.BaseUserDetail;
import com.legendshop.common.security.dto.ShopUserDetail;
import com.legendshop.common.security.enums.LegendshopSecurityErrorEnum;
import com.legendshop.console.auth.server.handler.AbstractUserInfoHandler;
import com.legendshop.user.api.ShopUserApi;
import com.legendshop.user.dto.BasisLoginParamsDTO;
import com.legendshop.user.dto.SysUserDTO;
import com.legendshop.user.dto.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 商家登录处理类
 *
 * @author legendshop
 */
@Component("SHOP")
public class ShopHandler extends AbstractUserInfoHandler {

	@Autowired
	private ShopUserApi shopUserApi;

	private final String userType = UserTypeEnum.SHOP.getLoginType();

	@Override
	public UserDetails buildUserDetails(String username) throws UsernameNotFoundException {
		UserInfo userInfo = buildUserInfo(username);
		Collection<? extends GrantedAuthority> authorities = processRolePermissions(userInfo.getRoles(), userInfo.getPermissions());

		SysUserDTO user = userInfo.getSysUser();
		return new ShopUserDetail(user.getId(), user.getShopId(), user.getUsername(), user.getMobile(), SecurityConstants.BCRYPT + user.getPassword(), userType,
				user.getAvatar(), user.getDelFlag(), true, true, user.getLockFlag(), authorities);
	}

	@Override
	public UserDetails buildUserDetails(BasisLoginParamsDTO loginParams) throws UsernameNotFoundException {
		UserDetails userDetails = super.buildUserDetails(loginParams);
		if (null != userDetails) {
			return userDetails;
		}
		R<UserInfo> handleResult = socialLoginApi.handle(loginParams);
		if (!handleResult.getSuccess()) {
			throw LegendshopSecurityErrorEnum.codeValue(handleResult.getMsg()).getException();
		}
		UserInfo userInfo = handleResult.getData();
		Collection<? extends GrantedAuthority> authorities = processRolePermissions(userInfo.getRoles(), userInfo.getPermissions());
		SysUserDTO user = userInfo.getSysUser();
		return new ShopUserDetail(user.getId(), user.getShopId(), user.getUsername(), user.getMobile(), SecurityConstants.BCRYPT + user.getPassword(), userType,
				user.getAvatar(), user.getDelFlag(), true, true, user.getLockFlag(), authorities);
	}

	@Override
	public void userLock(String username) {
		shopUserApi.updateStatusByUserName(username, Boolean.FALSE);
	}

	@Override
	public Map<String, Object> buildTokenEnhancer(BaseUserDetail baseUserDetail) {
		ShopUserDetail shopSecurityUserDetail = (ShopUserDetail) baseUserDetail;
		additionalInfo = new ConcurrentHashMap<>(6);
		additionalInfo.put("user_id", shopSecurityUserDetail.getUserId());
		if (shopSecurityUserDetail.getShopId() != null) {
			additionalInfo.put("shop_id", shopSecurityUserDetail.getShopId());
		}
		additionalInfo.put("user_type", userType);
		additionalInfo.put("user_name", shopSecurityUserDetail.getUsername());
		additionalInfo.put("user_mobile", shopSecurityUserDetail.getMobile());

		if (StringUtils.isNotBlank(shopSecurityUserDetail.getAvatar())) {
			additionalInfo.put("avatar", shopSecurityUserDetail.getAvatar());
		}
		return additionalInfo;
	}

	@Override
	protected UserInfo buildUserInfo(String username) throws UsernameNotFoundException {
		R<UserInfo> userInfo = shopUserApi.getUserInfo(username);
		if (!userInfo.getSuccess()) {
			throw LegendshopSecurityErrorEnum.codeValue(userInfo.getMsg()).getException();
		}
		return userInfo.getData();
	}
}
