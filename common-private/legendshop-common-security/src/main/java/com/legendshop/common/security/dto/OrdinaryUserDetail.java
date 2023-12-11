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
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;

/**
 * 普通用户详情
 *
 * @author legendshop
 */
public class OrdinaryUserDetail extends BaseUserDetail {

	@Serial
	private static final long serialVersionUID = -8985140245554804596L;
	/**
	 * 手机号
	 */
	@Getter
	@Setter
	private String mobile;

	@Getter
	@Setter
	private String nickname;

	public OrdinaryUserDetail(Long userId, String username, String mobile, String nickname, String password, String userType, String avatar, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(userId, username, password, userType, avatar, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.mobile = mobile;
		this.nickname = nickname;
	}


}
