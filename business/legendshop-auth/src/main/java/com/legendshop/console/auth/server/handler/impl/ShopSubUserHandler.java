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
import com.legendshop.common.security.dto.ShopSubUserDetail;
import com.legendshop.common.security.enums.LegendshopSecurityErrorEnum;
import com.legendshop.console.auth.server.handler.AbstractUserInfoHandler;
import com.legendshop.user.api.ShopSubUserApi;
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
@Component("SHOP_SUB_USER")
public class ShopSubUserHandler extends AbstractUserInfoHandler {

	@Autowired
	private ShopSubUserApi shopSubUserApi;

	private final String userType = UserTypeEnum.SHOP_SUB_USER.getLoginType();

	@Override
	public UserDetails buildUserDetails(String username) throws UsernameNotFoundException {
		UserInfo userInfo = buildUserInfo(username);
		Collection<? extends GrantedAuthority> authorities = processRolePermissions(userInfo.getRoles(), userInfo.getPermissions());
		SysUserDTO user = userInfo.getSysUser();
		return new ShopSubUserDetail(user.getShopUserId(), user.getShopId(), user.getId(), user.getUsername(), SecurityConstants.BCRYPT + user.getPassword(), userType, user.getAvatar(),
				user.getDelFlag(), true, true, user.getLockFlag(), authorities);
	}

	@Override
	public void userLock(String username) {
		shopSubUserApi.updateStatusByUserName(username, Boolean.FALSE);
	}

	@Override
	public Map<String, Object> buildTokenEnhancer(BaseUserDetail baseUserDetail) {
		ShopSubUserDetail shopSecurityUserDetail = (ShopSubUserDetail) baseUserDetail;
		additionalInfo = new ConcurrentHashMap<>(6);
		additionalInfo.put("shop_id", shopSecurityUserDetail.getShopId());
		additionalInfo.put("user_id", shopSecurityUserDetail.getUserId());
		additionalInfo.put("shop_sub_user_id", shopSecurityUserDetail.getShopSubUserId());
		additionalInfo.put("user_type", userType);

		if (StringUtils.isNotBlank(shopSecurityUserDetail.getAvatar())) {
			additionalInfo.put("avatar", shopSecurityUserDetail.getAvatar());
		}

		additionalInfo.put("user_name", shopSecurityUserDetail.getUsername());
		return additionalInfo;
	}

	@Override
	protected UserInfo buildUserInfo(String username) throws UsernameNotFoundException {
		R<UserInfo> userInfo = shopSubUserApi.getUserInfo(username);
		if (!userInfo.getSuccess()) {
			throw LegendshopSecurityErrorEnum.codeValue(userInfo.getMsg()).getException();
		}
		return userInfo.getData();
	}
}
