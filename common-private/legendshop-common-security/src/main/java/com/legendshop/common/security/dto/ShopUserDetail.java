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
public class ShopUserDetail extends BaseUserDetail {


	private static final long serialVersionUID = -4780425133484174251L;
	@Getter
	@Setter
	private Long shopId;

	@Getter
	@Setter
	private String mobile;

	/**
	 * 商家账号初始化
	 */
	public ShopUserDetail(Long userId, Long shopId, String username, String mobile, String password, String userType, String avatar, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(userId, username, password, userType, avatar, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.shopId = shopId;
		this.mobile = mobile;
	}
}
