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
public class OrderCancelExportDTO {

	@ColumnWidth(30)
	@ExcelProperty("订单号")
	@Schema(description = "订单号")
	private String orderNumber;

	@ColumnWidth(30)
	@ExcelProperty("售后编号")
	@Schema(description = "售后编号")
	private String refundNumber;

	@ColumnWidth(40)
	@ExcelProperty("订单商品规格属性")
	@Schema(description = "订单商品规格属性")
	private String productAttribute;

	@ColumnWidth(30)
	@ExcelProperty("申请时间")
	@Schema(description = "申请时间")
	private String createTime;

	@ColumnWidth(30)
	@ExcelProperty("订单金额")
	@Schema(description = "订单金额")
	private String orderMoney;

	@ColumnWidth(30)
	@ExcelProperty("商品名称")
	@Schema(description = "商品名称")
	private String productName;


	@ColumnWidth(45)
	@ExcelProperty("操作账号/时间")
	@Schema(description = "操作账号/时间")
	private String accountTime;

	@ColumnWidth(30)
	@ExcelProperty("取消原因")
	@Schema(description = "取消原因")
	private String reason;

	@ColumnWidth(30)
	@ExcelProperty("备注说明")
	@Schema(description = "备注说明")
	private String sellerMessage;

	@ColumnWidth(30)
	@ExcelProperty("状态")
	@Schema(description = "状态")
	private String status;

	@ColumnWidth(30)
	@ExcelProperty("备注")
	@Schema(description = "备注")
	private String adminMessage;
}
