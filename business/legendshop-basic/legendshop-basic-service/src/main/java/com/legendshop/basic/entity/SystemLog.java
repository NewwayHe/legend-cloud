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
import lombok.Data;

import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_system_log")
public class SystemLog implements GenericEntity<Long> {

	private static final long serialVersionUID = -9124726907676005144L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "SYS_LOG_SEQ")
	private Long id;

	/**
	 * 日志标题
	 */
	@Column(name = "title")
	private String title;

	/**
	 * 请求url
	 */
	@Column(name = "request_uri")
	private String requestUri;

	/**
	 * 请求来源
	 */
	@Column(name = "source")
	private String source;

	/**
	 * 请求服务
	 */
	@Column(name = "service")
	private String service;


	/**
	 * 调用ip
	 */
	@Column(name = "remote_ip")
	private String remoteIp;

	/**
	 * 请求方法类型
	 */
	@Column(name = "method")
	private String method;


	/**
	 * userAgent
	 */
	@Column(name = "user_agent")
	private String userAgent;

	/**
	 * 传递的参数
	 */
	@Column(name = "params")
	private String params;

	/**
	 * 请求执行的时间
	 */
	@Column(name = "time")
	private Long time;

	/**
	 * 异常信息
	 */
	@Column(name = "code")
	private Integer code;

	/**
	 * 请求用户
	 */
	@Column(name = "request_user")
	private String requestUser;

	/**
	 * 请求用户类型
	 */
	@Column(name = "user_type")
	private String userType;

	/**
	 * 请求用户ID
	 */
	@Column(name = "userId")
	private Long userId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

}
