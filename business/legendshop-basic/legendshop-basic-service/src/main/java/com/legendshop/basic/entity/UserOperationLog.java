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
import com.legendshop.basic.enums.UserOperationLogSideEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_user_operation_log")
public class UserOperationLog implements GenericEntity<Long> {

	private static final long serialVersionUID = 1430346137886978971L;
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "USER_OPERATION_LOG_SEQ")
	private Long id;

	/**
	 * 用户手机号，默认为0
	 */
	@Column(name = "user_mobile")
	private String userMobile;

	/**
	 * 用户名
	 */
	@Column(name = "user_name")
	private String userName;

	/**
	 * 操作名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 操作编码
	 */
	@Column(name = "code")
	private String code;

	/**
	 * 操作类型
	 */
	@Column(name = "type")
	private String type;


	/**
	 * 主要请求地址
	 */
	@Column(name = "url")
	private String url;

	/**
	 * 发起时间
	 */
	@Column(name = "time")
	private Date time;

	/**
	 * 操作系统
	 */
	@Column(name = "os")
	private String os;

	/**
	 * 浏览器名称
	 */
	@Column(name = "browser_name")
	private String browserName;

	/**
	 * 浏览器版本
	 */
	@Column(name = "browser_version")
	private String browserVersion;


	/**
	 * {@link UserOperationLogSideEnum}
	 */
	@Column(name = "side")
	private String side;
}
