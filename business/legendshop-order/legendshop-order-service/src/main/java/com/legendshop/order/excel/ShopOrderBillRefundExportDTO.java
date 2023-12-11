/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 账单退款订单
 *
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
@Schema(description = "账单退款订单")
public class ShopOrderBillRefundExportDTO implements Serializable {

	private static final long serialVersionUID = -5124767250532353695L;

	@ExcelIgnore
	@Schema(description = "订单ID")
	private Long orderId;

	@ColumnWidth(30)
	@ExcelProperty("订单编号")
	@Schema(description = "订单编号")
	private String orderNumber;

	@ColumnWidth(30)
	@ExcelProperty("下单时间")
	@Schema(description = "下单时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date orderTime;

	@ColumnWidth(30)
	@ExcelProperty("售后编号")
	@Schema(description = "售后编号")
	private String refundNumber;

	@ColumnWidth(30)
	@ExcelProperty("申请时间")
	@Schema(description = "申请时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	@ColumnWidth(30)
	@ExcelProperty("退款金额")
	@Schema(description = "退款金额")
	private BigDecimal refundAmount;
}
