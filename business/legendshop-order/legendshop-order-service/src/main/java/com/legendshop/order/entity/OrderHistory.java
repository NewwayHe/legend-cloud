/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

import java.util.Date;

/**
 * 订购历史表(OrderHistory)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_order_history")
public class OrderHistory implements GenericEntity<Long> {

	private static final long serialVersionUID = 702482456234536743L;

	/**
	 * ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "ORDER_HIST_SEQ")
	private Long id;


	/**
	 * 订购ID
	 */
	@Column(name = "order_id")
	private Long orderId;


	/**
	 * 订单状态
	 */
	@Column(name = "status")
	private String status;


	/**
	 * 修改时间
	 */
	@Column(name = "create_time")
	private Date createTime;


	/**
	 * 操作的动作信息 如: xxoo 于 2015-05-21 15:40:23 提交订单
	 */
	@Column(name = "reason")
	private String reason;

}
