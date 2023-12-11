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
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * (PayRefundSettlement)实体类
 *
 * @author legendshop
 * @since 2021-05-12 18:09:16
 */
@Data
@Entity
@Table(name = "ls_pay_refund_settlement")
public class PayRefundSettlement extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 262439136708610080L;


	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "payRefundSettlement_SEQ")
	private Long id;

	/**
	 * 支付退款流水号
	 */
	@Column(name = "pay_refund_sn")
	private String payRefundSn;

	/**
	 * 订单号
	 */
	@Column(name = "order_number")
	private String orderNumber;

	/**
	 * 退款金额
	 */
	@Column(name = "refund_amount")
	private BigDecimal refundAmount;

	/**
	 * 支付金额
	 */
	@Column(name = "pay_amount")
	private BigDecimal payAmount;

	/**
	 * 支付来源
	 */
	@Column(name = "pay_source")
	private String paySource;

	/**
	 * 支付退款类型
	 */
	@Column(name = "pay_refund_type")
	private String payRefundType;

	/**
	 * 状态
	 */
	@Column(name = "state")
	private Integer state;

	/**
	 * 支付单号
	 */
	@Column(name = "settlement_sn")
	private String settlementSn;

	/**
	 * 退款单号
	 */
	@Column(name = "refund_sn")
	private String refundSn;

	/**
	 * 外部退款单号
	 */
	@Column(name = "external_refund_sn")
	private String externalRefundSn;

	/**
	 * 退款结果描述
	 */
	@Column(name = "result_desc")
	private String resultDesc;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 修改时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

}
