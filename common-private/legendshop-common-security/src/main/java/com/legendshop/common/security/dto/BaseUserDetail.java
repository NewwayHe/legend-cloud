/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.security.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.io.Serial;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author legendshop
 * 扩展用户信息
 */
public class BaseUserDetail extends User implements OAuth2AuthenticatedPrincipal {

	@Serial
	private static final long serialVersionUID = 6709954268827848574L;

	/**
	 * 扩展属性，方便存放oauth 上下文相关信息
	 */
	private final Map<String, Object> attributes = new HashMap<>();

	/**
	 * 用户id，如果是商家，则为商家id
	 */
	@Getter
	@Setter
	private Long userId;

	/**
	 * 用户类型
	 */
	@Getter
	@Setter
	private String userType;

	/**
	 * 头像
	 */
	@Getter
	@Setter
	private String avatar;

	/**
	 * 用户名
	 */
	@Getter
	@Setter
	private String username;


	/**
	 * Construct the <code>User</code> with the details required by
	 * {@link DaoAuthenticationProvider}.
	 * <p>
	 * the username presented to the
	 * <code>DaoAuthenticationProvider</code>
	 *
	 * @param password              the password that should be presented to the
	 *                              <code>DaoAuthenticationProvider</code>
	 * @param enabled               set to <code>true</code> if the user is enabled
	 * @param accountNonExpired     set to <code>true</code> if the account has not expired
	 * @param credentialsNonExpired set to <code>true</code> if the credentials have not expired
	 * @param accountNonLocked      set to <code>true</code> if the account is not locked
	 * @param authorities           the authorities that should be granted to the caller if they
	 *                              presented the correct username and password and the user is
	 *                              enabled. Not null.
	 * @throws IllegalArgumentException if a <code>null</code> value was passed either as a parameter or
	 *                                  as an element in the <code>GrantedAuthority</code> collection
	 */
	public BaseUserDetail(Long userId, String username, String password, String userType, String avatar,
						  boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
						  Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.userId = userId;
		this.userType = userType;
		this.avatar = avatar;
		this.username = username;
	}


	/**
	 * Get the OAuth 2.0 token attributes
	 *
	 * @return the OAuth 2.0 token attributes
	 */
	@Override
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	@Override
	public String getName() {
		return this.getUsername();
	}
}
