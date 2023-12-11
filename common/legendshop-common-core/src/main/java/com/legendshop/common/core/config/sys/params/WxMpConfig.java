/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.config.sys.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 微信商户平台
 *
 * @author legendshop
 */
@Data
public class WxMpConfig extends WxConfig {

	private static final long serialVersionUID = -7972601817777355515L;


	/**
	 * 授权作用域,
	 * snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid）,
	 * snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且， 即使在未关注的情况下，只要用户授权，也能获取其信息 ）
	 */
	@JsonProperty(value = "scope")
	private String scope;


	/**
	 * 获取推送接口的token地址
	 */
	@JsonProperty(value = "wxPushTokenUrl")
	private String wxPushTokenUrl;

	/**
	 * 推送地址
	 */
	private String wxPushSendUrl;

	/**
	 * 开启原路退款
	 */
	private Boolean refundFlag;

	/**
	 * 退款证书
	 */
	private String refundFile;

	/**
	 * api密钥
	 */
	private String partnerKey;

	/**
	 * token
	 */
	private String token;

	/**
	 * 商户号
	 */
	private String mchId;

	/**
	 * 启用
	 */
	private Boolean enabled;

}
