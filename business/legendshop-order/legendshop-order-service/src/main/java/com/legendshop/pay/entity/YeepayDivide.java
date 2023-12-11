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
 * 易宝支付分账信息(YeepayDivide)实体类
 *
 * @author legendshop
 * @since 2021-03-31 20:51:36
 */
@Data
@Entity
@Table(name = "ls_yeepay_divide")
public class YeepayDivide extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 156897465298438142L;

	/**
	 * ID
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "yeepayDivide_SEQ")
	private Long id;

	/**
	 * 店铺ID
	 */
	@Column(name = "shop_id")
	private Long shopId;

	/**
	 * 订单ID
	 */
	@Column(name = "order_id")
	private Long orderId;

	/**
	 * 易宝订单ID
	 */
	@Column(name = "yeepay_order_id")
	private Long yeepayOrderId;

	/**
	 * 分账请求ID
	 */
	@Column(name = "request_id")
	private String requestId;

	/**
	 * 分账接收方编号(接收分账资金的易宝商编)
	 */
	@Column(name = "ledger_no")
	private String ledgerNo;

	/**
	 * 分账金额
	 */
	@Column(name = "amount")
	private BigDecimal amount;

	/**
	 * 手续费
	 */
	@Column(name = "handling_fee")
	private BigDecimal handlingFee;

	/**
	 * 分账说明
	 */
	@Column(name = "divide_detail_desc")
	private String divideDetailDesc;

	/**
	 * 分账类型：apply 申请   complete 完结
	 */
	@Column(name = "type")
	private String type;

	/**
	 * 分账状态  PROCESSING:处理中 SUCCESS:分账成功 FAIL:分账失败
	 */
	@Column(name = "status")
	private String status;

	/**
	 * 易宝分账明细单号
	 */
	@Column(name = "divide_detail_no")
	private String divideDetailNo;

	/**
	 * 易宝收款订单号
	 */
	@Column(name = "unique_order_no")
	private String uniqueOrderNo;

}
