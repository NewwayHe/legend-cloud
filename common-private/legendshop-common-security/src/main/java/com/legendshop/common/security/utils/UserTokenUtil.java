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
import com.legendshop.common.core.constant.StringConstant;
import com.legendshop.common.security.dto.BaseUserDetail;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Objects;

/**
 * Token工具，根据token获取用户信息
 *
 * @author legendshop
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserTokenUtil {

	private final OAuth2AuthorizationService oAuth2AuthorizationService;

	/**
	 * 通过用户名称获取登录用户token
	 *
	 * @param clientId the 用户类型：user、shop、admin、app
	 * @param username the 登录用户的用户名称
	 * @return
	 */
	public String getUserToken(String clientId, String username) {
		return StringConstant.EMPTY;
	}

	public OAuth2Authorization getUserInfo(String token) {
		OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
		return oAuth2Authorization;
	}

	public Long getUserId(String token) {
		OAuth2Authorization userInfo = getUserInfo(token);
		if (null == userInfo) {
			return null;
		}
		BaseUserDetail baseUserDetail = oAuth2AuthorizationConvert(userInfo);
		if (ObjectUtil.isNull(baseUserDetail)) {
			return null;
		}
		return baseUserDetail.getUserId();
	}

	/**
	 * Token转换
	 *
	 * @param userInfo
	 * @return baseUserDetail
	 */
	private BaseUserDetail oAuth2AuthorizationConvert(OAuth2Authorization userInfo) {
		Object principal = Objects.requireNonNull(userInfo).getAttributes().get(Principal.class.getName());
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
		return (BaseUserDetail) usernamePasswordAuthenticationToken.getPrincipal();
	}

	public String getUserType(String token) {
		OAuth2Authorization userInfo = getUserInfo(token);
		if (null == userInfo) {
			return null;
		}
		return oAuth2AuthorizationConvert(userInfo).getUserType();
	}

	public String getUserName(String token) {
		OAuth2Authorization userInfo = getUserInfo(token);
		if (null == userInfo) {
			return null;
		}
		return oAuth2AuthorizationConvert(userInfo).getUsername();
	}

	/**
	 * 通过Request请求，获取用户ID
	 */
	public Long getUserId(HttpServletRequest request) {
		String header = request.getHeader(RequestHeaderConstant.AUTHORIZATION_KEY);
		if (header == null) {
			return null;//防止没有传递参数过来
		}
		String tokenValue = header.replace(OAuth2AccessToken.TokenType.BEARER.getValue(), StrUtil.EMPTY).trim();
		return getUserId(tokenValue);
	}

	/**
	 * 通过Request请求，获取用户访问令牌
	 */
	public OAuth2Authorization getUserInfo(HttpServletRequest request) {
		String header = request.getHeader(RequestHeaderConstant.AUTHORIZATION_KEY);
		if (header == null) {
			// 防止没有传递参数过来
			return null;
		}
		String tokenValue = header.replace(OAuth2AccessToken.TokenType.BEARER.getValue(), StrUtil.EMPTY).trim();
		return getUserInfo(tokenValue);
	}

	/**
	 * 通过Request请求，获取用户类型
	 */
	public String getUserType(HttpServletRequest request) {
		String header = request.getHeader(RequestHeaderConstant.AUTHORIZATION_KEY);
		if (header == null) {
			// 防止没有传递参数过来
			return null;
		}
		String tokenValue = header.replace(OAuth2AccessToken.TokenType.BEARER.getValue(), StrUtil.EMPTY).trim();
		return getUserType(tokenValue);
	}

	public String getUserName(HttpServletRequest request) {
		String header = request.getHeader(RequestHeaderConstant.AUTHORIZATION_KEY);
		if (header == null) {
			// 防止没有传递参数过来
			return null;
		}
		String tokenValue = header.replace(OAuth2AccessToken.TokenType.BEARER.getValue(), StrUtil.EMPTY).trim();
		return getUserName(tokenValue);
	}

}
