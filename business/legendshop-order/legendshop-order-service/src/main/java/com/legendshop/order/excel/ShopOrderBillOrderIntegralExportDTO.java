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
 * 商家订单结算订单
 *
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
@Schema(description = "商家积分订单结算订单")
public class ShopOrderBillOrderIntegralExportDTO implements Serializable {

	private static final long serialVersionUID = -2251647664322031168L;

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
	@ExcelProperty("支付时间")
	@Schema(description = "支付时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date payTime;

	@ColumnWidth(20)
	@ExcelProperty("订单金额")
	@Schema(description = "订单金额")
	private BigDecimal orderAmount;

	@ColumnWidth(20)
	@ExcelProperty("运费金额")
	@Schema(description = "运费金额")
	private BigDecimal freightAmount;

	@ColumnWidth(20)
	@ExcelProperty("红包金额")
	@Schema(description = "红包金额")
	private BigDecimal redpackAmount;

	@ColumnWidth(20)
	@ExcelProperty("退款金额")
	@Schema(description = "退款金额")
	private BigDecimal refundAmount;

	@ColumnWidth(20)
	@ExcelProperty("抵扣金额")
	@Schema(description = "抵扣金额")
	private BigDecimal totalDeductionAmount;

	@ColumnWidth(20)
	@ExcelProperty("使用积分")
	@Schema(description = "使用积分")
	private BigDecimal totalIntegral;

	@ColumnWidth(20)
	@ExcelProperty("积分结算金额")
	@Schema(description = "积分结算金额")
	private BigDecimal settlementPrice;

	@ColumnWidth(20)
	@ExcelProperty("兑换积分比例")
	@Schema(description = "兑换积分,比例 x:1")
	private String proportion;
}
