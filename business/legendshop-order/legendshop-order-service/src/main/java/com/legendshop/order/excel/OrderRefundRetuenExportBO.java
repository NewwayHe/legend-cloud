/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.excel;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
public class OrderRefundRetuenExportBO {

	/**
	 * 订单编号
	 */
	private String subNumber;
	/**
	 * 类型
	 */
	private String applyType;
	/**
	 * 退款金额
	 */
	private BigDecimal refundAmount;
	/**
	 * 退单时间
	 */
	private Date applyTime;
	/**
	 * 退单红包金额
	 */
	private BigDecimal returnRedpackOffPrice;
	/**
	 * 完成时间
	 */
	private Date adminTime;

}
