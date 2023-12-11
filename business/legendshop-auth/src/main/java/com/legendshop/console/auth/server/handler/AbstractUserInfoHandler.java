/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.console.auth.server.handler;

import cn.hutool.core.util.ArrayUtil;
import com.legendshop.common.core.constant.RequestHeaderConstant;
import com.legendshop.common.core.constant.SecurityConstants;
import com.legendshop.common.core.enums.AuthTypeEnum;
import com.legendshop.user.api.SocialLoginApi;
import com.legendshop.user.dto.BasisLoginParamsDTO;
import com.legendshop.user.dto.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AbstractUserInfoHandler
 *
 * @author legendshop
 */
public abstract class AbstractUserInfoHandler implements UserInfoHandler {

	private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	public Map<String, Object> additionalInfo;

	@Autowired
	protected SocialLoginApi socialLoginApi;

	/**
	 * UserInfo
	 *
	 * @param username the 用户名
	 * @return userInfo
	 */
	protected abstract UserInfo buildUserInfo(String username) throws UsernameNotFoundException;


	@Override
	public UserDetails buildUserDetails(BasisLoginParamsDTO loginParams) throws UsernameNotFoundException {

		additionalInfo = new ConcurrentHashMap<>(6);

		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (null != requestAttributes) {
			HttpServletRequest request = requestAttributes.getRequest();
			loginParams.setSource(request.getHeader(RequestHeaderConstant.SOURCE_KEY));
		}

		// 是否为密码登录
		if (!loginParams.getAuthType().equals(AuthTypeEnum.PASSWORD)) {
			return null;
		}
		// 获取用户信息
		UserDetails userDetails = buildUserDetails(loginParams.getPrincipal());
		// 密码校验
		if (!passwordEncoder.matches(loginParams.getCredentials(), userDetails.getPassword())) {
			// TODO 线上会出现返回英文的情况
			throw new BadCredentialsException("用户名或密码错误");
		}
		return userDetails;
	}

	/**
	 * 处理角色和权限
	 *
	 * @param roles       the 角色列表
	 * @param permissions the 权限列表
	 * @return the GrantedAuthority
	 */
	public Collection<? extends GrantedAuthority> processRolePermissions(Long[] roles, String[] permissions) {
		Set<String> dbAuthsSet = new HashSet<>();
		if (ArrayUtil.isNotEmpty(roles)) {
			// 获取角色
			Arrays.stream(roles).forEach(roleId -> dbAuthsSet.add(SecurityConstants.ROLE + roleId));
			// 获取资源
			dbAuthsSet.addAll(Arrays.asList(permissions));
		}
		return AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));
	}
}
