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

/**
 * 退货退款详情表(OrderRefundReturnDetail)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_order_refund_return_detail")
public class OrderRefundReturnDetail implements GenericEntity<Long> {

	private static final long serialVersionUID = 271786156516815828L;

	/**
	 * ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "ORDER_REFUND_RETURN_DETAIL_SEQ")
	private Long id;


	/**
	 * 退款单ID
	 */
	@Column(name = "refund_id")
	private Long refundId;

	/**
	 * 第三方退款单号
	 */
	@Column(name = "out_refund_no")
	private String outRefundNo;

	/**
	 * 退款类型id
	 */
	@Column(name = "refund_type")
	private String refundType;


	/**
	 * 支付类型
	 */
	@Column(name = "pay_type_id")
	private String payTypeId;


	/**
	 * 退款金额
	 */
	@Column(name = "refund_amount")
	private BigDecimal refundAmount;


	/**
	 * 退款状态，0 失败， 1成功， 2进行中
	 */
	@Column(name = "refund_flag")
	private Integer refundFlag;


	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;

}
