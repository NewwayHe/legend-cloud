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
 * 操作日志表(OperatorLog)实体类
 *
 * @author legendshop
 * @since 2023-08-29 14:13:56
 */
@Data
@Entity
@Table(name = "ls_operator_log")
public class OperatorLog implements GenericEntity<Long> {

	private static final long serialVersionUID = -96392516406936079L;

	/**
	 * ID
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "OPERATOR_LOG_SEQ")
	private Long id;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 用户名称
	 */
	@Column(name = "user_name")
	private String userName;

	/**
	 * 用户类型：UserTypeEnum
	 */
	@Column(name = "user_type")
	private String userType;

	/**
	 * 事件类型：EventTypeEnum
	 */
	@Column(name = "event_type")
	private String eventType;

	/**
	 * 修改前
	 */
	@Column(name = "before_modification")
	private String beforeModification;

	/**
	 * 修改后
	 */
	@Column(name = "after_modification")
	private String afterModification;

	/**
	 * 操作人ID
	 */
	@Column(name = "operator_id")
	private Long operatorId;

	/**
	 * 操作人名称
	 */
	@Column(name = "operator_name")
	private String operatorName;

	/**
	 * 操作人类型：UserTypeEnum
	 */
	@Column(name = "operator_type")
	private String operatorType;

	/**
	 * 所属IP
	 */
	@Column(name = "remote_ip")
	private String remoteIp;

	/**
	 * 请求来源：VisitSourceEnum
	 */
	@Column(name = "source")
	private String source;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

}
