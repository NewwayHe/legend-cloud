/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
public class OrderRefundExportDTO {

	@ColumnWidth(30)
	@ExcelProperty("订单号")
	@Schema(description = "订单号")
	private String orderNumber;

	@ColumnWidth(30)
	@ExcelProperty("售后编号")
	@Schema(description = "售后编号")
	private String refundNumber;

	@ColumnWidth(30)
	@ExcelProperty("申请时间")
	@Schema(description = "申请时间")
	private String createTime;

	@ColumnWidth(30)
	@ExcelProperty("售后类型")
	@Schema(description = "售后类型")
	private String applyType;

	@ColumnWidth(30)
	@ExcelProperty("商品名称")
	@Schema(description = "商品名称")
	private String productName;

	@ColumnWidth(30)
	@ExcelProperty("订单状态")
	@Schema(description = "订单状态")
	private String orderStatus;

	@ColumnWidth(30)
	@ExcelProperty("收货人信息")
	@Schema(description = "收货人信息")
	private String receiverInfo;

	@ColumnWidth(30)
	@ExcelProperty("订单金额")
	@Schema(description = "订单金额")
	private String orderAmount;

	@ColumnWidth(30)
	@ExcelProperty("退款金额")
	@Schema(description = "退款金额")
	private String refundAmount;

	@ColumnWidth(30)
	@ExcelProperty("退款状态")
	@Schema(description = "退款状态")
	private String refundStatus;


}
