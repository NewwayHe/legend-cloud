/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * (OauthClientDetails)实体类
 *
 * @author legendshop
 */
@Data
public class OauthClientDetailsDTO implements Serializable {

	private static final long serialVersionUID = -198831487516339673L;
	/**
	 * 应用id
	 */
	private String clientId;


	private String resourceIds;


	private String clientSecret;


	/**
	 * 权限范围
	 */
	private String scope;


	/**
	 * 授权类型
	 */
	private String authorizedGrantTypes;


	/**
	 * web应用回调页面
	 */
	private String webServerRedirectUri;


	private String authorities;


	/**
	 * token有效期
	 */
	private Integer accessTokenValidity;


	/**
	 * 刷新token有效期
	 */
	private Integer refreshTokenValidity;


	private String additionalInformation;


	private String autoapprove;


	/**
	 * 所租用户
	 */
	private Integer tenantId;

}
