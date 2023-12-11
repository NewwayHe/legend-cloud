/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.console.auth.server.service.impl;

import cn.hutool.core.util.StrUtil;
import com.legendshop.common.core.enums.UserTypeEnum;
import com.legendshop.console.auth.server.service.TokenHandleService;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author legendshop
 */
@AllArgsConstructor
@Service
public class TokenHandleServiceImpl implements TokenHandleService {

	private final CacheManager cacheManager;
	private final OAuth2AuthorizationService authorizationService;


	/**
	 * 删除Token
	 *
	 * @param token
	 * @param userType
	 * @return
	 */
	@Override
	public Boolean delToken(String token, String userType) {
		OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
		if (authorization == null) {
			return true;
		}

		OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
		if (accessToken == null || StrUtil.isBlank(accessToken.getToken().getTokenValue())) {
			return true;
		}
		// 清空token
		authorizationService.remove(authorization);
		// 清空用户缓存redis
		Cache cache = cacheManager.getCache(Objects.requireNonNull(UserTypeEnum.obtainCacheName(userType)));
		if (null == cache) {
			return true;
//			throw new BusinessException("get cache is null");
		}
		cache.evict(authorization.getPrincipalName());
		return true;
	}
}
