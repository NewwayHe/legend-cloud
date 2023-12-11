/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.wechat.storage;

import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 微信小程序扩展redis存储
 *
 * @author legendshop
 */
public class WxMaInRedisConfigStorage extends WxMaDefaultConfigImpl {

	private final static String ACCESS_TOKEN_KEY = "wechat_ma::access_token_";

	private final RedisTemplate<String, String> redisTemplate;

	private String accessTokenKey;

	public WxMaInRedisConfigStorage(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.accessTokenKey = ACCESS_TOKEN_KEY;
	}

	@Override
	public void setAppid(String appId) {
		super.setAppid(appId);
	}

	@Override
	public String getAccessToken() {
		return redisTemplate.opsForValue().get(this.accessTokenKey);
	}

	@Override
	public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
		redisTemplate.opsForValue().set(accessTokenKey, accessToken, expiresInSeconds - 200, TimeUnit.SECONDS);
	}

	@Override
	public void expireAccessToken() {
		redisTemplate.expire(this.accessTokenKey, 0, TimeUnit.SECONDS);
	}

	@Override
	public boolean isAccessTokenExpired() {
		return redisTemplate.getExpire(accessTokenKey) < 2L;
	}

	public void setAccessTokenKey(String accessTokenKey) {
		this.accessTokenKey = accessTokenKey;
	}
}
