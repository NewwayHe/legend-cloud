/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

/**
 * 支付单据项(orderSettlementItem)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_pay_settlement_order")
public class PaySettlementOrder implements GenericEntity<Long> {

	private static final long serialVersionUID = -87217805630866838L;


	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PAY_SETTLEMENT_ORDER_SEQ")
	private Long id;

	/**
	 * 商户系统内部的支付单据号
	 */
	@Column(name = "pay_settlement_sn")
	private String paySettlementSn;


	/**
	 * 订单流水号
	 */
	@Column(name = "order_number")
	private String orderNumber;


	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;

}
