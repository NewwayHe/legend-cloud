/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.config;

import cn.hutool.json.JSONUtil;
import com.legendshop.basic.service.SysParamsService;
import com.legendshop.common.core.config.sys.params.WxConfig;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.wechat.model.enums.WeChatSourceEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 启动basic服务保存数据库的路由信息到redis
 *
 * @author legendshop
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class WeChatConfigInitRunner implements ApplicationRunner {

	private final SysParamsService sysParamsService;

	private final RedisTemplate<String, String> redisTemplate;


	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("初始化微信配置信息");
		for (WeChatSourceEnum value : WeChatSourceEnum.values()) {
			WxConfig config = this.sysParamsService.getNotCacheConfigByName(value.name(), WxConfig.class);
			if (null == config) {
				return;
			}
			this.redisTemplate.opsForValue().set(CacheConstants.WECHAT_CONFIG_KEY + "_" + value.name(), JSONUtil.toJsonStr(config));
			this.redisTemplate.convertAndSend(CacheConstants.WECHAT_CONFIG_RELOAD_TOPIC, value.name());
		}
		log.info("动态加载微信配置信息 ");
	}
}
