/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

/**
 * (OauthClientDetails)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_oauth_client_details")
public class OauthClientDetails implements GenericEntity<String> {

	private static final long serialVersionUID = -52931748855341541L;

	/**
	 * 应用id
	 */
	@Id
	@Column(name = "client_id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "OAUTH_CLIENT_DETAILS_SEQ")
	private String clientId;


	@Column(name = "resource_ids")
	private String resourceIds;


	@Column(name = "client_secret")
	private String clientSecret;


	/**
	 * 权限范围
	 */
	@Column(name = "scope")
	private String scope;


	/**
	 * 授权类型
	 */
	@Column(name = "authorized_grant_types")
	private String authorizedGrantTypes;


	/**
	 * web应用回调页面
	 */
	@Column(name = "web_server_redirect_uri")
	private String webServerRedirectUri;


	@Column(name = "authorities")
	private String authorities;


	/**
	 * token有效期
	 */
	@Column(name = "access_token_validity")
	private Integer accessTokenValidity;


	/**
	 * 刷新token有效期
	 */
	@Column(name = "refresh_token_validity")
	private Integer refreshTokenValidity;


	@Column(name = "additional_information")
	private String additionalInformation;


	@Column(name = "autoapprove")
	private String autoapprove;


	/**
	 * 所租用户
	 */
	@Column(name = "tenant_id")
	private Integer tenantId;

	@Override
	@Transient
	public String getId() {
		return clientId;
	}

	@Override
	public void setId(String id) {
		this.clientId = id;
	}

}
