/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author legendshop
 */
public class BasisLoginDTO {

	@Schema(description = "认证标识:(手机号、用户名、授权码)")
	private String principal;

	@Schema(description = "安全凭证:(密码、验证码、校验码)")
	private String credentials;

	@Schema(description = "第三方用户标识:(openId,aliId)")
	private String thirdPartyIdentifier;

	@Schema(description = "认证类型:(password,mobilePhoneCode,weChatApp,weChatApp)")
	private String authType;

	@Schema(description = "登录用户类:(USER,ADMIN,SHOP,SHOP_SUB_USER)")
	private String userType;
}
