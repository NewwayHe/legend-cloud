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

/**
 * 易宝支付订单信息(YeepayOrder)实体类
 *
 * @author legendshop
 * @since 2021-03-26 15:59:26
 */
@Data
@Entity
@Table(name = "ls_yeepay_order")
public class YeepayOrder extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 908700514887435356L;

	/**
	 * ID
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "yeepayOrder_SEQ")
	private Long id;

	/**
	 * 易宝订单号
	 */
	@Column(name = "order_number")
	private String orderNumber;

	/**
	 * 订单token
	 */
	@Column(name = "token")
	private String token;

	/**
	 * 发起方商编
	 */
	@Column(name = "parent_merchant_no")
	private String parentMerchantNo;

	/**
	 * 支付流水号（内部编号）
	 */
	@Column(name = "pay_settlement_sn")
	private String paySettlementSn;

	/**
	 * 渠道侧商户请求号
	 */
	@Column(name = "bank_order_id")
	private String bankOrderId;

	/**
	 * 预支付标识信息
	 */
	@Column(name = "pre_pay_in")
	private String prePayIn;

	/**
	 * 易宝收款订单号
	 */
	@Column(name = "unique_order_no")
	private String uniqueOrderNo;

	/**
	 * 合单支付
	 */
	@Column(name = "combined_order")
	private Boolean combinedOrder;

	/**
	 * 手续费
	 */
	@Column(name = "fee")
	private BigDecimal fee;

	/**
	 * 手续费承担方商编
	 * 平台商承担时返回平台商商编
	 * 商户承担时返回商户编号
	 */
	@Column(name = "fee_undertaker_merchant_no")
	private String feeUndertakerMerchantNo;

	/**
	 * 到账类型
	 * REAL_TIME:实时到账
	 * TWO_HOUR:2 小时到账
	 * NEXT_DAY:次日到账（无特殊情况资金于次日上午 7 点左右到银行账户中）
	 */
	@Column(name = "receive_type")
	private String receiveType;

	/**
	 * 返回收款账户-银行账号
	 */
	@Column(name = "receiver_account_no")
	private String receiverAccountNo;

	/**
	 * 返回收款账户-开户名称
	 */
	@Column(name = "receiver_account_name")
	private String receiverAccountName;

	/**
	 * 返回收款账户-开户行编码
	 */
	@Column(name = "bank_code")
	private String bankCode;

	/**
	 * 冲退标识
	 * 由于银行原因，会发生付款已到账又通知冲退的情况（付款资金会原路退回到商户账户）
	 * 示例值：true
	 */
	@Column(name = "is_reversed")
	private Boolean isReversed;

	/**
	 * 返回银行通知冲退的时间
	 */
	@Column(name = "reverse_time")
	private String reverseTime;
}
