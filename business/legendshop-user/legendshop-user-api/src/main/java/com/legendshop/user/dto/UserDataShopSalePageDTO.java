/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
@Schema(description = "店铺销售排行分页DTO")
public class UserDataShopSalePageDTO {

	@ExcelIgnore
	private Long shopId;

	@ColumnWidth(30)
	@ExcelProperty("店铺名称")
	@Schema(description = "店铺名称")
	private String shopName;

	@ColumnWidth(30)
	@ExcelProperty("交易额")
	@Schema(description = "交易额")
	private BigDecimal dealAmount;

	@ColumnWidth(30)
	@ExcelProperty("交易订单数")
	@Schema(description = "交易订单数")
	private Integer dealOrderNum;

	@ColumnWidth(30)
	@ExcelProperty("交易商品数")
	@Schema(description = "交易商品数")
	private Integer dealGoodNum;

	@ColumnWidth(30)
	@ExcelProperty("退款金额")
	@Schema(description = "退款金额")
	private BigDecimal refundAmount;

	@ColumnWidth(30)
	@ExcelProperty("售后订单数")
	@Schema(description = "售后订单数")
	private Integer refundOrderNum;

	@ColumnWidth(30)
	@ExcelProperty("订单响应时间")
	@Schema(description = "订单响应时间")
	private BigDecimal orderResponseTime;

	@ColumnWidth(30)
	@ExcelProperty("缺货率")
	@Schema(description = "缺货率")
	private BigDecimal outStockRate;

	@ColumnWidth(30)
	@ExcelProperty("下架率")
	@Schema(description = "下架率")
	private BigDecimal undercarriageRate;

}
