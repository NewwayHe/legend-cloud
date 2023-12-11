/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
public class MarketingDataTrendDTO implements Serializable {

	/**
	 * 查询时间
	 */
	@ColumnWidth(30)
	@ExcelProperty("时间")
	@Schema(description = "时间")
	private String time;

	/**
	 * 店铺新增成交金额
	 */
	@ColumnWidth(30)
	@ExcelProperty("新增成交金额")
	@Schema(description = "新增成交金额")
	BigDecimal newTransactionAmount;

	/**
	 * 店铺累计成交金额
	 */
	@ColumnWidth(30)
	@ExcelProperty("累计成交金额")
	@Schema(description = "累计成交金额")
	BigDecimal transactionAmountList;

	/**
	 * 店铺新增成交订单数
	 */
	@ColumnWidth(30)
	@ExcelProperty("新增成交订单数")
	@Schema(description = "新增成交订单数")
	Long newAddOrderCount;

	/**
	 * 店铺累计成交订单数
	 */
	@ColumnWidth(30)
	@ExcelProperty("累计成交订单数")
	@Schema(description = "累计成交订单数")
	Long orderCountList;

	/**
	 * 店铺新增成交商品数量
	 */
	@ColumnWidth(30)
	@ExcelProperty("新增成交商品数量")
	@Schema(description = "新增成交商品数量")
	Long newAddDealCount;

	/**
	 * 店铺累计成交商品数量
	 */
	@ColumnWidth(30)
	@ExcelProperty("累计成交商品数量")
	@Schema(description = "累计成交商品数量")
	Long dealCount;

	/**
	 * 店铺新增下单用户数
	 */
	@ColumnWidth(30)
	@ExcelProperty("新增下单用户数")
	@Schema(description = "新增下单用户数")
	Long newOrderUserCount;

	/**
	 * 店铺累计下单用户数
	 */
	@ColumnWidth(30)
	@ExcelProperty("累计下单用户数")
	@Schema(description = "累计下单用户数")
	Long orderUserCountList;

	/**
	 * 访问用户数
	 */
	@ColumnWidth(30)
	@ExcelProperty("新增访问用户数")
	@Schema(description = "新增访问用户数")
	Long newVisitUserCount;

	/**
	 * 访问次数
	 */
	@ColumnWidth(30)
	@ExcelProperty("新增访问次数")
	@Schema(description = "新增访问次数")
	Long newVisitCount;

	/**
	 * 访问用户数
	 */
	@ColumnWidth(30)
	@ExcelProperty("累计用户数")
	@Schema(description = "累计用户数")
	Long visitUserCountList;

	/**
	 * 访问次数
	 */
	@ColumnWidth(30)
	@ExcelProperty("累计访问次数")
	@Schema(description = "累计访问次数")
	Long visitCountList;

	/**
	 * 新增领券人数
	 */
	@ColumnWidth(30)
	@ExcelProperty("新增领券人数")
	@Schema(description = "新增领券人数")
	private Long newReceivedUserCount;

	/**
	 * 累计领券人数
	 */
	@ColumnWidth(30)
	@ExcelProperty("累计领券人数")
	@Schema(description = "累计领券人数")
	private Long totalReceivedUserCount;

	public MarketingDataTrendDTO() {
		this.newTransactionAmount = BigDecimal.ZERO;
		this.transactionAmountList = BigDecimal.ZERO;
		this.newAddOrderCount = 0L;
		this.orderCountList = 0L;
		this.newAddDealCount = 0L;
		this.dealCount = 0L;
		this.newOrderUserCount = 0L;
		this.orderUserCountList = 0L;
		this.newVisitUserCount = 0L;
		this.newVisitCount = 0L;
		this.visitUserCountList = 0L;
		this.visitCountList = 0L;
		this.newReceivedUserCount = 0L;
		this.totalReceivedUserCount = 0L;
	}
}
