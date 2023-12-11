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

import java.math.BigDecimal;

/**
 * 用于数据导出
 *
 * @author legendshop
 */
@Data
public class OrderExcelWriterDTO {

	private String orderNumber;
	private String createDate;
	private String userTel;
	private String userMobile;
	private String userEmail;
	private String invoiceType;
	private String receiverName;
	private String receiverMobile;
	private String receiverTel;
	private String receiverAddr;
	private String remark;
	private BigDecimal actualTotal;
	private BigDecimal freeAmount;
	private String invoiceInfo;
}
