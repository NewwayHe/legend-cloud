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
import java.util.Date;

/**
 * 用户购买力数据表
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_data_user_purchasing")
public class DataUserPurchasing implements GenericEntity<Long> {

	private static final long serialVersionUID = -79180166752458384L;

	/**
	 * ID
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "DATAUSERPURCHASING_SEQ")
	private Long id;

	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 用户昵称
	 */
	@Column(name = "nick_name")
	private String nickName;

	/**
	 * 手机号
	 */
	@Column(name = "mobile")
	private String mobile;

	/**
	 * 下单金额
	 */
	@Column(name = "total_amount")
	private BigDecimal totalAmount;

	/**
	 * 支付金额
	 */
	@Column(name = "pay_amount")
	private BigDecimal payAmount;

	/**
	 * 成交金额
	 */
	@Column(name = "deal_amount")
	private BigDecimal dealAmount;

	/**
	 * 运费
	 */
	@Column(name = "freight_price")
	private BigDecimal freightPrice;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 来源
	 */
	@Column(name = "source")
	private String source;

	/**
	 * 商品数量
	 */
	@Column(name = "quantity")
	private Integer quantity;


	/**
	 * 店铺id
	 */
	@Column(name = "shop_id")
	private Long shopId;

	/**
	 * 订单id
	 */
	@Column(name = "order_id")
	private Long orderId;

}
