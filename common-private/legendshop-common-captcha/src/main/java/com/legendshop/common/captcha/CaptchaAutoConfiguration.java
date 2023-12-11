/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.captcha;

import com.legendshop.common.captcha.config.CaptchaServiceConfiguration;
import com.legendshop.common.captcha.properties.CaptchaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 验证码自动初始化类
 *
 * @author legendshop
 */
@Configuration
@EnableConfigurationProperties(CaptchaProperties.class)
@ComponentScan("com.legendshop.common.captcha")
@Import({CaptchaServiceConfiguration.class})
public class CaptchaAutoConfiguration {

}
