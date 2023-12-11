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
import com.legendshop.common.core.enums.AuthTypeEnum;
import com.legendshop.common.core.enums.UserTypeEnum;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.common.security.dto.BaseUserDetail;
import com.legendshop.common.security.dto.OrdinaryUserDetail;
import com.legendshop.common.security.enums.LegendshopSecurityErrorEnum;
import com.legendshop.common.security.exception.NotBindUserException;
import com.legendshop.console.auth.server.handler.AbstractUserInfoHandler;
import com.legendshop.user.api.OrdinaryUserApi;
import com.legendshop.user.dto.BasisLoginParamsDTO;
import com.legendshop.user.dto.SysUserDTO;
import com.legendshop.user.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户登录处理类
 *
 * @author legendshop
 */
@Component("USER")
@RequiredArgsConstructor
public class UserHandler extends AbstractUserInfoHandler {

	private final OrdinaryUserApi ordinaryUserApi;

	private final String userType = UserTypeEnum.USER.getLoginType();

	@Override
	protected UserInfo buildUserInfo(String username) throws UsernameNotFoundException {
		R<UserInfo> userInfo = ordinaryUserApi.getUserInfo(username);
		if (!userInfo.getSuccess()) {
			throw LegendshopSecurityErrorEnum.codeValue(userInfo.getMsg()).getException();
		}
		return userInfo.getData();
	}

	@Override
	public UserDetails buildUserDetails(String username) throws UsernameNotFoundException {
		UserInfo userInfo = buildUserInfo(username);

		Collection<? extends GrantedAuthority> authorities = processRolePermissions(userInfo.getRoles(), userInfo.getPermissions());

		SysUserDTO user = userInfo.getSysUser();
		return new OrdinaryUserDetail(user.getId(), user.getUsername(), user.getMobile(), user.getNickname(),
				SecurityConstants.BCRYPT + user.getPassword(), userType, user.getAvatar(),
				user.getDelFlag(), true, true, user.getLockFlag(), authorities);
	}

	@Override
	public Map<String, Object> buildTokenEnhancer(BaseUserDetail baseUserDetail) {

		OrdinaryUserDetail securityUserDetail = (OrdinaryUserDetail) baseUserDetail;
		additionalInfo = new ConcurrentHashMap<>(6);
		additionalInfo.put("user_id", securityUserDetail.getUserId());
		additionalInfo.put("user_type", userType);
		additionalInfo.put("user_name", securityUserDetail.getUsername());
		additionalInfo.put("user_mobile", securityUserDetail.getMobile());
		additionalInfo.put("nick_name", securityUserDetail.getNickname());

		if (StringUtils.isNotBlank(securityUserDetail.getAvatar())) {
			additionalInfo.put("avatar", securityUserDetail.getAvatar());
		}

		return additionalInfo;
	}

	@Override
	public UserDetails buildUserDetails(BasisLoginParamsDTO loginParams) throws UsernameNotFoundException {
		UserDetails userDetails = super.buildUserDetails(loginParams);
		if (null != userDetails) {
			// 更新用户授权信息
			//PASSWORD 登录模式，当前是有可能是在微信公众号，小程序，后期可能还会有APP，H5。在使用密码登录时，带上OPENID信息，则更新用户在当前模式下的Passport ,
			//调用更新前，切换验证模式【当前只有小程序需要实现，后续按需求优化】
			if (null != loginParams.getSource() && loginParams.getSource().equals(VisitSourceEnum.MINI.name())) {
				loginParams.setAuthType(AuthTypeEnum.WECHAT_MINI);
				socialLoginApi.updateUserOpenId(loginParams);
			}

			return userDetails;
		}
		R<UserInfo> handleResult = socialLoginApi.handle(loginParams);
		if (!handleResult.getSuccess()) {
			if (handleResult.getCode() == HttpStatus.CREATED.value()) {
				throw new NotBindUserException(handleResult.getMsg());
			}
			throw LegendshopSecurityErrorEnum.codeValue(handleResult.getMsg()).getException();
		}
		UserInfo userInfo = handleResult.getData();
		SysUserDTO user = userInfo.getSysUser();
		Collection<? extends GrantedAuthority> authorities = processRolePermissions(userInfo.getRoles(), userInfo.getPermissions());
		return new OrdinaryUserDetail(user.getId(), user.getUsername(), user.getMobile(), user.getNickname(),
				SecurityConstants.BCRYPT + user.getPassword(), userType, user.getAvatar(),
				user.getDelFlag(), true, true, user.getLockFlag(), authorities);
	}

	@Override
	public void userLock(String username) {
		ordinaryUserApi.updateStatusByUserName(username, Boolean.FALSE);
	}

}
