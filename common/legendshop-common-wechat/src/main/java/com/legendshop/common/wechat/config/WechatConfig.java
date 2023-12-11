/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.wechat.config;

import cn.hutool.json.JSONUtil;
import com.legendshop.common.core.config.sys.params.WxConfig;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.wechat.WechatSDKClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.annotation.Async;

/**
 * 启动basic服务保存数据库的路由信息到redis
 *
 * @author legendshop
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class WechatConfig {

	private final StringRedisTemplate redisTemplate;

	@Bean
	public RedisMessageListenerContainer weChatRedisContainer(RedisConnectionFactory redisConnectionFactory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);
		container.addMessageListener((message, bytes) -> {
			log.info("刷新微信配置事件触发！刷新类型 message: {}", message);
			String config = this.redisTemplate.opsForValue().get(CacheConstants.WECHAT_CONFIG_KEY + "_" + message);
			WxConfig wxConfig = JSONUtil.toBean(config, WxConfig.class);
			WechatSDKClient.configReload(wxConfig, message.toString(), redisTemplate);
		}, new ChannelTopic(CacheConstants.WECHAT_CONFIG_RELOAD_TOPIC));
		return container;
	}

	@Async
	@Order
	@EventListener(WebServerInitializedEvent.class)
	public void initClient() {
		WechatSDKClient.setRedisTemplate(redisTemplate);
	}

}
