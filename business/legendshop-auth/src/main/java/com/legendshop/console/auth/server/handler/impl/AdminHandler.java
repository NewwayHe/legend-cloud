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
import com.legendshop.common.security.dto.AdminUserDetail;
import com.legendshop.common.security.dto.BaseUserDetail;
import com.legendshop.common.security.enums.LegendshopSecurityErrorEnum;
import com.legendshop.console.auth.server.handler.AbstractUserInfoHandler;
import com.legendshop.user.api.AdminUserApi;
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
 * 管理员登录处理类
 *
 * @author legendshop
 */
@Component("ADMIN")
public class AdminHandler extends AbstractUserInfoHandler {

	private final String userType = UserTypeEnum.ADMIN.getLoginType();

	@Autowired
	private AdminUserApi adminUserApi;

	@Override
	public UserInfo buildUserInfo(String username) throws UsernameNotFoundException {
		R<UserInfo> userInfo = this.adminUserApi.getUserInfo(username);
		if (!userInfo.getSuccess()) {
			throw LegendshopSecurityErrorEnum.codeValue(userInfo.getMsg()).getException();
		}
		return userInfo.getData();
	}

	@Override
	public Map<String, Object> buildTokenEnhancer(BaseUserDetail baseUserDetail) {
		AdminUserDetail adminSecurityUserDetail = (AdminUserDetail) baseUserDetail;
		additionalInfo = new ConcurrentHashMap<>(6);
		additionalInfo.put("user_id", adminSecurityUserDetail.getUserId());
		additionalInfo.put("user_name", adminSecurityUserDetail.getUsername());

		if (StringUtils.isNotBlank(adminSecurityUserDetail.getAvatar())) {
			additionalInfo.put("avatar", adminSecurityUserDetail.getAvatar());
		}
		additionalInfo.put("user_type", userType);
		return additionalInfo;
	}

	@Override
	public UserDetails buildUserDetails(String username) throws UsernameNotFoundException {
		UserInfo userInfo = buildUserInfo(username);
		Collection<? extends GrantedAuthority> authorities = processRolePermissions(userInfo.getRoles(), userInfo.getPermissions());
		SysUserDTO user = userInfo.getSysUser();
		return new AdminUserDetail(user.getId(), user.getUsername(), SecurityConstants.BCRYPT + user.getPassword(), userType, user.getAvatar(), user.getDeptId(),
				user.getDelFlag(), true, true, user.getLockFlag(), authorities);
	}

	@Override
	public void userLock(String username) {
		adminUserApi.updateStatusByUserName(username, Boolean.FALSE);
	}


}
