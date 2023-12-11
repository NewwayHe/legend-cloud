/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.wechat.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信配置
 *
 * @author legendshop
 */
@Data
@ConfigurationProperties("legendshop.wechat")
public class WechatProperties {

	/**
	 * mp 是公众号
	 */
	private WechatMpProperties mp = new WechatMpProperties();

	/**
	 * ma是小程序
	 */
	private WechatMaProperties ma = new WechatMaProperties();


	/**
	 * 公众号配置
	 */
	@Data
	public static class WechatMpProperties {

		/**
		 * appId
		 */
		private String appId = "";

		/**
		 * app密钥
		 */
		private String appSecret = "";

		/**
		 * 授权作用域  ，
		 * snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），
		 * snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且， 即使在未关注的情况下，只要用户授权，也能获取其信息 ）
		 */
		private String scope = "snsapi_userinfo";
	}


	/**
	 * 小程序配置
	 */
	@Data
	public static class WechatMaProperties {

		/**
		 * appId
		 */
		private String appId = "";

		/**
		 * app密钥
		 */
		private String appSecret = "";
	}


}
