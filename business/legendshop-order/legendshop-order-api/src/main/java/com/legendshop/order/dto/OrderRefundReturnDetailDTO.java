/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 退货退款详情表(OrderRefundReturnDetail)实体类
 *
 * @author legendshop
 */
@Data
public class OrderRefundReturnDetailDTO implements Serializable {

	private static final long serialVersionUID = 271786156516815828L;

	/**
	 * 主键id
	 */
	private Long id;

	/**
	 * 退款单ID
	 */
	private Long refundId;

	/**
	 * 第三方退款单号
	 */
	private String outRefundNo;

	/**
	 * 退款类型id
	 */
	private String refundTypeId;


	/**
	 * 支付类型
	 */
	private String payType;


	/**
	 * 退款金额
	 */
	private BigDecimal refundAmount;


	/**
	 * 退款状态，0 失败， 1成功
	 */
	private Integer refundStatus;


	/**
	 * 备注
	 */
	private String remark;

}
