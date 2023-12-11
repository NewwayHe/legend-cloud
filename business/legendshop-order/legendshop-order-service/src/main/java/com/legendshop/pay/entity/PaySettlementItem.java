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
import com.legendshop.common.core.dto.BaseEntity;
import com.legendshop.pay.enums.PaySettlementStateEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付单据项(orderSettlementItem)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_pay_settlement_item")
public class PaySettlementItem extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -87217805630866838L;


	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PAY_SETTLEMENT_ITEM_SEQ")
	private Long id;

	/**
	 * 商户系统内部的支付单据号
	 */
	@Column(name = "pay_settlement_sn")
	private String paySettlementSn;


	/**
	 * 业务编码：第三方返回
	 */
	@Column(name = "transaction_sn")
	private String transactionSn;


	/**
	 * 支付方式编号
	 */
	@Column(name = "pay_type_id")
	private String payTypeId;


	/**
	 * 第三方支付单金额
	 */
	@Column(name = "amount")
	private BigDecimal amount;


	/**
	 * 退款金额
	 */
	@Column(name = "refund_amount")
	private BigDecimal refundAmount;

	/**
	 * 支付项状态 {@link PaySettlementStateEnum}
	 */
	@Column(name = "state")
	private Integer state;


}
