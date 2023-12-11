/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.security.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.legendshop.common.core.constant.RequestHeaderConstant;
import com.legendshop.common.core.constant.SecurityConstants;
import com.legendshop.common.core.enums.UserTypeEnum;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.common.security.dto.AdminUserDetail;
import com.legendshop.common.security.dto.BaseUserDetail;
import com.legendshop.common.security.dto.OrdinaryUserDetail;
import com.legendshop.common.security.dto.ShopUserDetail;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 安全工具类
 *
 * @author legendshop
 */
@UtilityClass
@Slf4j
public class SecurityUtils {

	public VisitSourceEnum getUserRequestSource() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (null == attributes) {
			return VisitSourceEnum.UNKNOWN;
		}
		HttpServletRequest request = attributes.getRequest();
		String source = request.getHeader(RequestHeaderConstant.SOURCE_KEY);
		return VisitSourceEnum.getByName(source);
	}

	/**
	 * 获取Authentication
	 */
	public Authentication getAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (ObjectUtil.isEmpty(authentication)) {
			log.error("认证失败，没有认证信息");

			return null;
		}
		return authentication;
	}

	/**
	 * 获取用户信息
	 *
	 * @param authentication the authentication
	 * @return BaseUserDetail
	 */
	public BaseUserDetail getUser(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		if ("anonymousUser".equals(principal)) {
			principal = null;
		}
		return (BaseUserDetail) principal;

	}

	public BaseUserDetail getBaseUser() {
		Authentication authentication = getAuthentication();
		return getUser(authentication);

	}

	/**
	 * 获取管理员用户
	 */
	public AdminUserDetail getAdminUser() {
		Authentication authentication = getAuthentication();
		return (AdminUserDetail) getUser(authentication);
	}

	/**
	 * 获取普通用户
	 */
	public OrdinaryUserDetail getUser() {
		Authentication authentication = getAuthentication();
		return (OrdinaryUserDetail) getUser(authentication);
	}

	/**
	 * 获取商家用户
	 */
	public ShopUserDetail getShopUser() {
		Authentication authentication = getAuthentication();
		return (ShopUserDetail) getUser(authentication);
	}

	/**
	 * 获取普通用户
	 */
	public Long getUserId() {
		Authentication authentication = getAuthentication();
		if (null == authentication) {
			return null;
		}
		if (ObjectUtil.isEmpty(getUser(authentication))) {
			return null;
		}
		return getUser(authentication).getUserId();
	}

	public String getUserName() {
		BaseUserDetail baseUser = getBaseUser();
		if (null == baseUser) {
			return null;
		}
		return baseUser.getUsername();
	}

	public String getUserType() {
		BaseUserDetail baseUser = getBaseUser();
		if (null == baseUser) {
			return null;
		}
		return baseUser.getUserType();
	}

	/**
	 * 获取普通用户
	 */
	public String getUserMobile() {
		BaseUserDetail baseUser = getBaseUser();
		if (null == baseUser) {
			return null;
		}
		UserTypeEnum userTypeEnum = UserTypeEnum.valueOf(baseUser.getUserType());
		if (userTypeEnum.equals(UserTypeEnum.USER)) {
			return ((OrdinaryUserDetail) baseUser).getMobile();
		} else if (userTypeEnum.equals(UserTypeEnum.SHOP)) {
			return ((ShopUserDetail) baseUser).getMobile();
		}
		return null;
	}

	/**
	 * 获取用户角色信息
	 *
	 * @return 角色集合
	 */
	public List<Long> getRoles() {
		Authentication authentication = getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		List<Long> roleIds = new ArrayList<>();
		authorities.stream().filter(granted -> StrUtil.startWith(granted.getAuthority(), SecurityConstants.ROLE))
				.forEach(granted -> {
					String id = StrUtil.removePrefix(granted.getAuthority(), SecurityConstants.ROLE);
					roleIds.add(Long.parseLong(id));
				});
		return roleIds;
	}

}
