/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.captcha.config;

import com.anji.captcha.model.common.Const;
import com.anji.captcha.service.CaptchaCacheService;
import com.anji.captcha.service.CaptchaService;
import com.anji.captcha.service.impl.CaptchaServiceFactory;
import com.legendshop.common.captcha.properties.CaptchaProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 验证码配置类
 *
 * @author legendshop
 */
@Configuration
public class CaptchaServiceConfiguration {


	/**
	 * 初始化图片验证的service
	 *
	 * @param prop
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public CaptchaService captchaService(CaptchaProperties prop) {
		Properties config = new Properties();
		config.put(Const.CAPTCHA_CACHETYPE, prop.getCacheType().name());
		config.put(Const.CAPTCHA_WATER_MARK, prop.getWaterMark());
		config.put(Const.CAPTCHA_FONT_TYPE, prop.getFontType());
		config.put(Const.CAPTCHA_TYPE, prop.getType().getCodeValue());
		config.put(Const.CAPTCHA_INTERFERENCE_OPTIONS, prop.getInterferenceOptions());
		config.put(Const.ORIGINAL_PATH_PIC_CLICK, prop.getPicClick());
		config.put(Const.CAPTCHA_SLIP_OFFSET, prop.getSlipOffset());
		config.put(Const.CAPTCHA_AES_STATUS, prop.getAesStatus());
		config.put(Const.CAPTCHA_WATER_FONT, prop.getWaterFont());
		config.put(Const.CAPTCHA_CACAHE_MAX_NUMBER, prop.getCacheNumber());
		config.put(Const.CAPTCHA_TIMING_CLEAR_SECOND, prop.getTimingClear());
		return CaptchaServiceFactory.getInstance(config);
	}

	/**
	 * 个初始化验证码缓存service
	 *
	 * @param properties
	 * @return
	 */
	@Bean
	public CaptchaCacheService captchaCacheService(CaptchaProperties properties) {
		return CaptchaServiceFactory.getCache(properties.getCacheType().name());
	}

}
