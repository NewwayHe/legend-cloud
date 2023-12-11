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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 账单分销佣金
 *
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
@Schema(description = "账单分销佣金")
public class ShopOrderBillDistributionExportDTO implements Serializable {

	private static final long serialVersionUID = 7886561681182406359L;

	@ExcelIgnore
	@Schema(description = "订单ID")
	private Long orderItemId;

	@ColumnWidth(30)
	@ExcelProperty("订单编号")
	@Schema(description = "订单编号")
	private String orderNumber;

	@ExcelIgnore
	@Schema(description = "商品图片")
	private String pic;

	@ColumnWidth(30)
	@ExcelProperty("商品名称")
	@Schema(description = "商品名称")
	private String productName;

	@ColumnWidth(30)
	@ExcelProperty("产品动态属性")
	@Schema(description = "产品动态属性")
	private String attribute;

	@ColumnWidth(30)
	@ExcelProperty("购买数量")
	@Schema(description = "购买数量")
	private Integer basketCount;

	@ColumnWidth(20)
	@ExcelProperty("商品金额（元）")
	@Schema(description = "商品金额")
	private BigDecimal actualAmount;

	@ColumnWidth(30)
	@ExcelProperty("分销比例（%）")
	@Schema(description = "分销比例")
	private BigDecimal distRatio;

	@ColumnWidth(20)
	@ExcelProperty("订单佣金（元）")
	@Schema(description = "订单佣金")
	private BigDecimal distCommissionCash;

	@ColumnWidth(30)
	@ExcelProperty("售后状态")
	@Schema(description = "售后状态")
	private String refundStatus;

	@ColumnWidth(30)
	@ExcelProperty("结算状态")
	@Schema(description = "结算状态")
	private String commissionSettleStatus;

}
