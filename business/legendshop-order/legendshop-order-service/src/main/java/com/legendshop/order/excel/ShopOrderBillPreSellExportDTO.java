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
 * 账单预售订单列表
 *
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
@Schema(description = "账单预售订单列表")
public class ShopOrderBillPreSellExportDTO implements Serializable {

	private static final long serialVersionUID = -315360419346856445L;

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

	@ColumnWidth(20)
	@ExcelProperty("订单金额")
	@Schema(description = "订单金额")
	private BigDecimal orderAmount;

	@ColumnWidth(20)
	@ExcelProperty("定金金额")
	@Schema(description = "定金金额")
	private BigDecimal preDepositAmount;

	@ColumnWidth(20)
	@ExcelProperty("尾款金额")
	@Schema(description = "尾款金额")
	private BigDecimal finalAmount;

	@ColumnWidth(20)
	@ExcelProperty("运费金额")
	@Schema(description = "运费金额")
	private BigDecimal freightAmount;
}
