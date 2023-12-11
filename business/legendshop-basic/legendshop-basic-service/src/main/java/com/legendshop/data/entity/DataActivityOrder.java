/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 活动订单表(DataActivityOrder)实体类
 *
 * @author legendshop
 * @since 2021-07-16 14:49:51
 */
@Data
@Entity
@Table(name = "ls_data_activity_order")
public class DataActivityOrder implements GenericEntity<Long> {

	private static final long serialVersionUID = -94724860515713372L;

	/**
	 * ID
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "dataActivityOrder_SEQ")
	private Long id;

	/**
	 * 活动id
	 */
	@Column(name = "activity_id")
	private Long activityId;

	/**
	 * 订单项id
	 */
	@Column(name = "order_item_id")
	private Long orderItemId;

	/**
	 * 订单id
	 */
	@Column(name = "order_id")
	private Long orderId;

	/**
	 * 实付金额
	 */
	@Column(name = "amount")
	private BigDecimal amount;

	/**
	 * 商品数量
	 */
	@Column(name = "basket_count")
	private Integer basketCount;

	/**
	 * 活动类型
	 */
	@Column(name = "activity_type")
	private String activityType;

}
