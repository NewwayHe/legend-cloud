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
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 预售订单表(PreSellOrder)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_pre_sell_order")
public class PreSellOrder extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -38490437415091485L;


	/**
	 * id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PRESELL_ORDER_SEQ")
	private Long id;

	/**
	 * 订购ID
	 */
	@Column(name = "order_id")
	private Long orderId;


	/**
	 * 支付方式,0:全额,1:定金
	 */
	@Column(name = "pay_pct_type")
	private Integer payPctType;


	/**
	 * 定金金额
	 */
	@Column(name = "pre_deposit_price")
	private BigDecimal preDepositPrice;


	/**
	 * 尾款金额
	 */
	@Column(name = "final_price")
	private BigDecimal finalPrice;


	/**
	 * 实付金额
	 * UPDATE ls_pre_sell_order SET actual_amount = 0;
	 * UPDATE ls_pre_sell_order lp SET actual_amount = pre_deposit_price + (SELECT IFNULL(lo.freight_price, 0) FROM ls_order lo WHERE lp.order_id = lo.id) WHERE pay_deposit_flag = 1 AND pay_pct_type = 0;
	 * UPDATE ls_pre_sell_order lp SET actual_amount = IF(pay_deposit_flag = 1, pre_deposit_price, 0) + IF(pay_final_flag = 1, final_price, 0) WHERE pay_pct_type = 1;
	 * UPDATE ls_pre_sell_order lp SET actual_amount = actual_amount + (SELECT IFNULL(lo.freight_price, 0) FROM ls_order lo WHERE lo.id = lp.order_id) WHERE pay_pct_type = 1 AND pay_deposit_flag = 1 AND pay_final_flag = 1;
	 */
	@Column(name = "actual_amount")
	private BigDecimal actualAmount;


	/**
	 * 定金支付开始时间
	 */
	@Column(name = "pre_sale_start")
	private Date preSaleStart;


	/**
	 * 定金支付结束时间
	 */
	@Column(name = "pre_sale_end")
	private Date preSaleEnd;


	/**
	 * 尾款支付开始时间
	 */
	@Column(name = "final_m_start")
	private Date finalMStart;


	/**
	 * 尾款支付结束时间
	 */
	@Column(name = "final_m_end")
	private Date finalMEnd;


	/**
	 * 预售发货开始时间
	 */
	@Column(name = "pre_delivery_time")
	private Date preDeliveryTime;


	/**
	 * 预售发货结束时间
	 */
	@Column(name = "pre_delivery_end_time")
	private Date preDeliveryEndTime;


	/**
	 * 定金支付时间
	 */
	@Column(name = "deposit_pay_time")
	private Date depositPayTime;


	/**
	 * 是否支付定金
	 */
	@Column(name = "pay_deposit_flag")
	private Boolean payDepositFlag;

	/**
	 * 支付方式 PayTypeEnum
	 */
	@Column(name = "deposit_pay_type")
	private String depositPayType;

	@Column(name = "final_pay_type")
	private String finalPayType;


	/**
	 * 支付尾款时间
	 */
	@Column(name = "pay_final_time")
	private Date payFinalTime;


	/**
	 * 是否支付尾款
	 */
	@Column(name = "pay_final_flag")
	private Boolean payFinalFlag;

}
