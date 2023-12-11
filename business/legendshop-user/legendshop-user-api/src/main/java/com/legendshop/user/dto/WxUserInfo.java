/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.legendshop.common.core.enums.VisitSourceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author legendshop
 */
@Data
@Schema(description = "微信用户信息")
@NoArgsConstructor
@AllArgsConstructor
public class WxUserInfo {

	/**
	 * ==============================================
	 * 微信返回基础字段
	 */

	@Schema(description = "普通用户的标识")
	@JsonProperty(value = "openid")
	private String openid;

	@Schema(description = "调用凭证")
	@JsonProperty(value = "access_token")
	private String access_token;

	@Schema(description = "普通用户昵称")
	@JsonProperty(value = "nickname")
	private String nickname;

	@Schema(description = "普通用户性别，1 为男性，2 为女性")
	@JsonProperty(value = "sex")
	private String sex;

	@Schema(description = "省份")
	@JsonProperty(value = "province")
	private String province;

	@Schema(description = "城市")
	@JsonProperty(value = "city")
	private String city;

	@Schema(description = "国家")
	@JsonProperty(value = "country")
	private String country;

	@Schema(description = "用户头像")
	@JsonProperty(value = "headimgurl")
	private String headimgurl;

	@Schema(description = "用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的 unionId 是唯一的")
	@JsonProperty(value = "unionid")
	private String unionid;

	@Schema(description = "会话密钥")
	@JsonProperty(value = "session_key")
	private String session_key;

	@Schema(description = "语言")
	@JsonProperty(value = "language")
	private String language;


	@JsonIgnore
	private String expires_in;
	@JsonIgnore
	private String refresh_token;
	@JsonIgnore
	private String scope;


	/**
	 * ==============================================
	 * Legendshop 自定义字段
	 */
	@Schema(description = "获取来源")
	@JsonIgnore
	private VisitSourceEnum source;

	@JsonIgnore
	private Object privilege;
}
