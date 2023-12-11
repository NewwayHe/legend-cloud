/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

/**
 * 社交登录的配置表
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_social_details")
public class SocialDetails extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -94255560368226353L;

	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "SOCIAL_DETAILS_SEQ")
	private Long id;


	/**
	 * 登录的类型
	 */
	@Column(name = "type")
	private String type;

	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;


	/**
	 * app_id
	 */
	@Column(name = "app_id")
	private String appId;


	/**
	 * app_secret
	 */
	@Column(name = "app_secret")
	private Integer appSecret;


	/**
	 * 回调url
	 */
	@Column(name = "redirect_url")
	private String redirectUrl;

	/**
	 * 是否启用
	 */
	@Column(name = "enable_flag")
	private Boolean enableFlag;

}
