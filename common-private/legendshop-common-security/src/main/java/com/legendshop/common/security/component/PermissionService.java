/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.security.component;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.legendshop.common.core.constant.SecurityConstants;
import com.legendshop.common.security.utils.TokenSignerHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;

/**
 * 接口权限判断工具
 *
 * @author legendshop
 */
@Slf4j
@Component("pms")
public class PermissionService {

	/**
	 * 判断接口是否有任意xxx，xxx权限
	 *
	 * @param permissions 权限
	 * @return {boolean}
	 */
	public boolean hasPermission(String... permissions) {
		if (ArrayUtil.isEmpty(permissions)) {
			return false;
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return false;
		}

		// 如果为feignClient方法调用，则直接跳过权限校验
		HttpServletRequest request = getRequest();
		String header = request.getHeader(SecurityConstants.FEIGN_PREFIX);
		if (StrUtil.isNotBlank(header) && TokenSignerHelper.checkToken(header)) {
			return true;
		}

		//判断当前用户权限列表是否有权限
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		boolean result = authorities.stream().map(GrantedAuthority::getAuthority).filter(StringUtils::hasText)
				.anyMatch(x -> PatternMatchUtils.simpleMatch(permissions, x));

		if (result) {
			return true;
		}

		//判断是否Feign Client 调用，可以跳过权限，进行内部Feign Client调用。
		//此处也可统一为由 request.getHeader(SecurityConstants.FEIGN_PREFIX); 进行校验，不需做二处判断。
		Object principal = authentication.getPrincipal();
		if (principal instanceof String && principal.equals(TokenSignerHelper.LEGENDSHOP_ANONYMOUS_ADMIN)) {
			return true;
		}

		return false;
	}

	/**
	 * 获取Request
	 */
	protected HttpServletRequest getRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attributes == null ? null : attributes.getRequest();
	}
}
