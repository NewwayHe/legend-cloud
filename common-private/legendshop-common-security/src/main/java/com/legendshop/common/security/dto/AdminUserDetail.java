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

import java.util.Collection;

/**
 * @author legendshop
 */
public class AdminUserDetail extends BaseUserDetail {

	/**
	 * 部门id
	 */
	@Getter
	@Setter
	private Long deptId;

	public AdminUserDetail(Long userId, String username, String password, String userType, String avatar, Long deptId, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(userId, username, password, userType, avatar, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.deptId = deptId;
	}
}
