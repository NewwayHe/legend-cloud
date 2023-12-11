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
@Table(name = "ls_sms_log")
public class SmsLog implements GenericEntity<Long> {

	private static final long serialVersionUID = 6107452623911259633L;
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "SMS_LOG_SEQ")
	private Long id;

	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 发送手机号
	 */
	@Column(name = "mobile_phone")
	private String mobilePhone;

	/**
	 * 发送类型
	 */
	@Column(name = "type")
	private Integer type;

	/**
	 * 发送内容
	 */
	@Column(name = "content")
	private String content;

	/**
	 * 响应状态
	 */
	@Column(name = "response_status")
	private String responseStatus;

	/**
	 * 发送时间
	 */
	@Column(name = "create_time")
	private Date createTime;


	/**
	 * 发送IP
	 */
	@Column(name = "request_ip")
	private String requestIp;


	/**
	 * 发送渠道 阿里云 腾讯
	 * 参考SmsType
	 */
	@Column(name = "channel_type")
	private String channelType;

}
