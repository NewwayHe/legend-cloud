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

import java.util.Date;

/**
 * 队列任务表(AmqpTask)实体类
 *
 * @author legendshop
 * @since 2022-04-29 14:16:52
 */
@Data
@Entity
@Table(name = "ls_amqp_task")
public class AmqpTask extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 950699191276355853L;

	/**
	 * id
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "amqpTask_SEQ")
	private Long id;

	/**
	 * 交换机
	 */
	@Column(name = "exchange")
	private String exchange;

	/**
	 * 路由键
	 */
	@Column(name = "routing_key")
	private String routingKey;

	/**
	 * 消息体
	 */
	@Column(name = "message")
	private String message;

	/**
	 * 延时时间
	 */
	@Column(name = "delay_time")
	private Date delayTime;

	/**
	 * 状态 0、未发 1、已发
	 */
	@Column(name = "status")
	private Integer status;

}
