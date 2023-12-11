/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.wechat;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.hutool.json.JSONUtil;
import com.legendshop.common.core.config.sys.params.WxConfig;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.wechat.model.enums.WeChatSourceEnum;
import com.legendshop.common.wechat.service.CommonWxService;
import com.legendshop.common.wechat.service.impl.CommonWxServiceImpl;
import com.legendshop.common.wechat.storage.WxMaInRedisConfigStorage;
import com.legendshop.common.wechat.storage.WxMpInRedisConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author legendshop
 */
public class WechatSDKClient {

	private static boolean init = false;
	private final static WxMaService wxMaService = new WxMaServiceImpl();
	private final static WxMpService wxMpService = new WxMpServiceImpl();
	private final static CommonWxService commonWxService = new CommonWxServiceImpl();
	private static RedisTemplate<String, String> redisTemplate = null;

	public static void configReload(WxConfig config, String type, RedisTemplate<String, String> redisTemplate) {
		switch (WeChatSourceEnum.valueOf(type)) {
			case WX_MINI_PRO:
				WxMaInRedisConfigStorage maConfig = new WxMaInRedisConfigStorage(redisTemplate);
				maConfig.setAppid(config.getAppId());
				maConfig.setSecret(config.getSecret());
				wxMaService.setWxMaConfig(maConfig);
				break;
			case WX_MP:
				WxMpInRedisConfigStorage mpConfig = new WxMpInRedisConfigStorage(redisTemplate);
				mpConfig.setAppId(config.getAppId());
				mpConfig.setSecret(config.getSecret());
				wxMpService.setWxMpConfigStorage(mpConfig);
				break;
		}
	}

	public static WxMaService wxMaService() {
		init(redisTemplate);
		return wxMaService;
	}

	public static WxMpService wxMpService() {
		init(redisTemplate);
		return wxMpService;
	}

	public static CommonWxService commonWxService() {
		return commonWxService;
	}

	public static void init(RedisTemplate<String, String> redisTemplate) {
		if (init) {
			return;
		}
		for (WeChatSourceEnum value : WeChatSourceEnum.values()) {
			String config = redisTemplate.opsForValue().get(CacheConstants.WECHAT_CONFIG_KEY + "_" + value.name());
			WxConfig wxConfig = JSONUtil.toBean(config, WxConfig.class);
			configReload(wxConfig, value.name(), redisTemplate);
		}
		init = true;
	}

	public static void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		WechatSDKClient.redisTemplate = redisTemplate;
	}
}
