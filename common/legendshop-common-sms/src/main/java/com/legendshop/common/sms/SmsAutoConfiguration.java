/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.sms;

import com.aliyuncs.IAcsClient;
import com.legendshop.common.sms.properties.SmsProperties;
import com.legendshop.common.sms.service.SmsSender;
import com.legendshop.common.sms.service.impl.AliyunSmsSenderImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 短信发送自动化配置类
 *
 * @author legendshop
 */
@EnableConfigurationProperties(SmsProperties.class)
@Configuration
@ComponentScan("com.legendshop.common.sms")
public class SmsAutoConfiguration {


	/**
	 * 注入阿里云的短信
	 */
	@Bean
	@ConditionalOnClass({IAcsClient.class})
	@ConditionalOnMissingBean
	public SmsSender aliSmsService(SmsProperties smsProperties) {
		if (smsProperties.getSmsType().equals(SmsProperties.SmsType.aliyun)) {
			return new AliyunSmsSenderImpl(smsProperties);
		}
		return null;
	}

	/**
	 * 注入腾讯云发送短信
	 */
//	@Configuration
//	@ConditionalOnClass({SmsSingleSender.class})
//	public static class TentcentSmsServiceConfiguration {
//
//		@Bean
//		public SmsSender tencentSmsService(SmsProperties smsProperties) {
//			if(smsProperties.getSmsType().equals(SmsProperties.SmsType.tencent)) {
//				return new TencentSmsSenderImpl(smsProperties);
//			}
//			return null;
//		}
//	}

}
