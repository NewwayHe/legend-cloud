/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import com.legendshop.common.core.enums.AuthTypeEnum;
import com.legendshop.common.core.enums.UserTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author legendshop
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasisLoginParamsDTO {
	@Schema(description = "认证标识:(手机号、用户名、授权码)")
	private String principal;

	@Schema(description = "安全凭证:(密码、验证码、校验码)")
	private String credentials;

	@Schema(description = "第三方用户标识:(openId,aliId,redisKey)")
	private String thirdPartyIdentifier;

	@Schema(description = "认证类型:(PASSWORD,SMS,WECHAT_PC,WECHAT_MP,WECHAT_MINI,WECHAT_APP,THIRD_PARTY_BIND)")
	private AuthTypeEnum authType;

	@Schema(description = "登录用户类:(USER,ADMIN,SHOP,SHOP_SUB_USER)")
	private UserTypeEnum userType;

	@Schema(description = "扩展字段:(小程序加密向量、公众号code)")
	private String extended;

	@Schema(description = "登录注册来源：pc、mini、mp等")
	private String source;

	@Schema(description = "登录注册 IP")
	private String ip;

	/**
	 * 游客ID
	 */
	@Schema(description = "游客id")
	private String visitorId;
}
