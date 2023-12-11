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
public class PayNotifyDTO {

	/**
	 * 结算单号
	 */
	private String settlementSn;

	/**
	 * 支付交易号
	 */
	private String transactionSn;

	/**
	 * 总金额
	 */
	private BigDecimal cashAmount;

	/**
	 * 支付是否成功
	 */
	private Boolean paySuccess;

	/**
	 * 支付类型
	 */
	private String payType;

}
