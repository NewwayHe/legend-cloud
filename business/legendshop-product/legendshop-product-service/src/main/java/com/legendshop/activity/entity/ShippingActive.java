/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 包邮活动表(ShippingActive)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_shipping_active")
public class ShippingActive implements GenericEntity<Long> {

	private static final long serialVersionUID = 698339211670384875L;

	/**
	 * 活动ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "SHIPPING_ACTIVE_SEQ")
	private Long id;


	/**
	 * 商家ID
	 */
	@Column(name = "shop_id")
	private Long shopId;


	/**
	 * 活动名称
	 */
	@Column(name = "name")
	private String name;


	/**
	 * 状态有效:1 下线：0
	 */
	@Column(name = "status")
	private Integer status;


	/**
	 * 店铺:0,商品:1
	 */
	@Column(name = "full_type")
	private Integer fullType;


	/**
	 * 类型1：元 2 件
	 */
	@Column(name = "reduce_type")
	private Integer reduceType;


	/**
	 * 满足的金额或件数 full_value
	 */
	@Column(name = "full_value")
	private BigDecimal fullValue;


	/**
	 * 优惠信息，用于活动列表展示
	 */
	@Column(name = "description")
	private String description;


	/**
	 * 开始时间
	 */
	@Column(name = "start_date")
	private Date startDate;


	/**
	 * 结束时间
	 */
	@Column(name = "end_date")
	private Date endDate;


	/**
	 * 创建时间
	 */
	@Column(name = "create_date")
	private Date createDate;


	@Transient
	private Long[] productIds;

}
