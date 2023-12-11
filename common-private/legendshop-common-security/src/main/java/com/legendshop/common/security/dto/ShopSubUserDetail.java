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
public class ShopSubUserDetail extends ShopUserDetail {

	private static final long serialVersionUID = -5243801476131107378L;
	/**
	 * 商家id
	 */
	@Getter
	@Setter
	private Long shopSubUserId;

	/**
	 * 商家子账号
	 */
	public ShopSubUserDetail(Long userId, Long shopId, Long shopSubUserId, String username, String password, String userType, String avatar, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(userId, shopId, username, null, password, userType, avatar, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.shopSubUserId = shopSubUserId;
	}
}
