/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
public class SystemLogDTO implements Serializable {

	private static final long serialVersionUID = -8954942810056160237L;

	private Long id;

	/**
	 * 日志标题
	 */
	private String title;

	/**
	 * 请求url
	 */
	private String requestUri;

	/**
	 * 请求来源
	 */
	private String source;

	/**
	 * 请求服务
	 */
	private String service;

	/**
	 * 调用ip
	 */
	private String remoteIp;

	/**
	 * 请求方法类型
	 */
	private String method;

	/**
	 * userAgent
	 */
	private String userAgent;

	/**
	 * 传递的参数
	 */
	private String params;

	/**
	 * 请求执行的时间
	 */
	private Long time;

	/**
	 * 请求结果编码
	 */
	private Integer code;

	/**
	 * 请求用户
	 */
	private String requestUser;

	/**
	 * 请求用户类型
	 */
	private String userType;

	/**
	 * 请求用户ID
	 */
	private Long userId;

	/**
	 * 创建时间
	 */
	private Date createTime;

}
