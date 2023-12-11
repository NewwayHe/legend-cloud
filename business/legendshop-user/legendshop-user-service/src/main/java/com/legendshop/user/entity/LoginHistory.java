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

import java.util.Date;

/**
 * 登录历史表(LoginHist)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_login_history")
public class LoginHistory implements GenericEntity<Long> {

	private static final long serialVersionUID = 165787727433247233L;

	/**
	 * ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "LOGIN_HIST_SEQ", allocationSize = 1)
	private Long id;


	/**
	 * 用户Id
	 */
	@Column(name = "user_id")
	private Long userId;


	/**
	 * 地区
	 */
	@Column(name = "area")
	private String area;


	/**
	 * 国家
	 */
	@Column(name = "country")
	private String country;


	/**
	 * IP
	 */
	@Column(name = "ip")
	private String ip;


	/**
	 * 时间
	 */
	@Column(name = "time")
	private Date time;


	/**
	 * 登录类型：USER,SELLER_TYPE,见LoginUserTypeEnum
	 */
	@Column(name = "login_type")
	private String loginType;


	/**
	 * 登录的来源,PC，MOBILE,APP, 见VisitSourceEnum
	 */
	@Column(name = "login_source")
	private String loginSource;

	/**
	 * 登录状态
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 用户昵称
	 */
	@Column(name = "nick_name")
	private String nickName;

}
