/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.hutool.json.JSONUtil;
import com.legendshop.basic.service.WechatConfigNoticeService;
import com.legendshop.common.core.config.sys.params.WxConfig;
import com.legendshop.common.core.constant.CacheConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author legendshop
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatConfigNoticeServiceImpl implements WechatConfigNoticeService {

	private final RedisTemplate<String, String> redisTemplate;

	@Override
	public void weChatConfigPush(String name, WxConfig config) {
		if (null == config) {
			return;
		}
		this.redisTemplate.opsForValue().set(CacheConstants.WECHAT_CONFIG_KEY + "_" + name, JSONUtil.toJsonStr(config));
		this.redisTemplate.convertAndSend(CacheConstants.WECHAT_CONFIG_RELOAD_TOPIC, name);
	}
}
