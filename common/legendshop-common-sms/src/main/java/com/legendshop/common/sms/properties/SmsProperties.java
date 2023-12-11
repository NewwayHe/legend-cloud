/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.sms.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ScopedProxyMode;

import static com.legendshop.common.sms.properties.SmsProperties.PREFIX;

/**
 * @author legendshop
 */
@Data
@ConfigurationProperties(prefix = PREFIX)
@RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
public class SmsProperties {

	public static final String PREFIX = "legendshop.sms";

	private AliyunSmsProperties aliyun = new AliyunSmsProperties();

	private TencentSmsProperties tencent = new TencentSmsProperties();

	/**
	 * 短信类型aliyun和tencent
	 */
	private SmsType smsType = SmsType.aliyun;

	@NoArgsConstructor
	public enum SmsType {
		/**
		 * 空
		 */
		na,
		/**
		 * 阿里云短信
		 */
		aliyun,
		/**
		 * 腾讯云短信
		 */
		tencent
	}

	@Data
	public static class AliyunSmsProperties {

		/**
		 * 阿里云的accessKey
		 */
		private String accessKey;

		/**
		 * 阿里云的secretKey
		 */
		private String secretKey;

		/**
		 * 签名名称
		 */
		private String signName = "";

		/**
		 * 地区id，默认为：cn-hangzhou
		 */
		private String regionId = "cn-hangzhou";

		/**
		 * 域名：默认为：dysmsapi.aliyuncs.com
		 */
		private String domain = "dysmsapi.aliyuncs.com";

		/**
		 * 版本
		 */
		private String version = "2017-05-25";
	}

	@Data
	public static class TencentSmsProperties {

		/**
		 * 腾讯云的appId
		 */
		private int appId;

		/**
		 * 腾讯云的appKey
		 */
		private String appKey;
	}
}
