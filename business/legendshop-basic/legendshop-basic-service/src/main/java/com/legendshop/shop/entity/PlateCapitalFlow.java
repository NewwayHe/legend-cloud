/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 平台资金流水(PlateCapitalFlow)实体类
 *
 * @author legendshop
 * @since 2020-09-18 17:26:09
 */
@Data
@Entity
@Table(name = "ls_plate_capital_flow")
public class PlateCapitalFlow implements GenericEntity<Long> {

	private static final long serialVersionUID = -14559708106597122L;

	/**
	 * id
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "plateCapitalFlow_SEQ")
	private Long id;

	/**
	 * 支付流水号
	 */
	@Column(name = "pay_settlement_sn")
	private String paySettlementSn;

	/**
	 * 收支类型
	 */
	@Column(name = "flow_type")
	private String flowType;

	/**
	 * 交易类型
	 */
	@Column(name = "deal_type")
	private String dealType;

	/**
	 * 支付方式
	 */
	@Column(name = "pay_type")
	private String payType;

	/**
	 * 支付方式
	 */
	@Column(name = "pay_type_name")
	private String payTypeName;

	/**
	 * 金额
	 */
	@Column(name = "amount")
	private BigDecimal amount;

	/**
	 * 记录时间
	 */
	@Column(name = "rec_date")
	private Date recDate;

	/**
	 * 支付时间
	 */
	@Column(name = "pay_time")
	private Date payTime;

	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;


	/**
	 * 详情id
	 */
	@Column(name = "detail_id")
	private Long detailId;

	/**
	 * 店铺ID
	 */
	@Column(name = "shop_id")
	private Long shopId;

	/**
	 * 店铺名称
	 */
	@Column(name = "shop_name")
	private String shopName;


}
