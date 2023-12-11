/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
public class RefundNotifyDTO {

	/**
	 * 退款类型（=支付类型）
	 */
	private String refundType;

	/**
	 * 系统内部退款单号
	 */
	private String refundSn;

	/**
	 * 第三方退款单号
	 */
	private String externalRefundSn;

	/**
	 * 总金额
	 */
	private BigDecimal refundAmount;

	/**
	 * 支付是否成功
	 */
	private Boolean refundStatus;

}
