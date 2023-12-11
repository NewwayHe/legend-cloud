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

import java.math.BigDecimal;
import java.util.Date;

/**
 * 结算档期(ShopBillPeriod)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_shop_bill_period")
public class ShopBillPeriod implements GenericEntity<Long> {

	private static final long serialVersionUID = 191848339464220373L;

	/**
	 * ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "SHOP_BILL_PERIOD_SEQ")
	private Long id;


	/**
	 * 档期标示，例如 201512-ID
	 */
	@Column(name = "flag")
	private String flag;


	/**
	 * 应结金额
	 */
	@Column(name = "result_total_amount")
	private BigDecimal resultTotalAmount;


	/**
	 * 这期订单的实际金额
	 */
	@Column(name = "order_amount")
	private BigDecimal orderAmount;


	/**
	 * 平台佣金
	 */
	@Column(name = "commis_totals")
	private BigDecimal commisTotals;


	/**
	 * 分销佣金
	 */
	@Column(name = "dist_commis_totals")
	private BigDecimal distCommisTotals;


	/**
	 * 退单金额
	 */
	@Column(name = "order_return_totals")
	private BigDecimal orderReturnTotals;


	/**
	 * 使用的红包总额
	 */
	@Column(name = "redpack_totals")
	private BigDecimal redpackTotals;

	/**
	 * 积分抵扣金额
	 */
	@Column(name = "total_deduction_amount")
	private BigDecimal totalDeductionAmount;


	/**
	 * 积分结算金额
	 */
	@Column(name = "total_settlement_price")
	private BigDecimal totalSettlementPrice;

	/**
	 * 建立时间
	 */
	@Column(name = "rec_date")
	private Date recDate;

}
