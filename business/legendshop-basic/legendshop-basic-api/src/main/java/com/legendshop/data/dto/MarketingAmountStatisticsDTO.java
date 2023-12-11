/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dto;

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
 * @author legendshop
 */
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
@Schema(description = "营销金额数据统计")
public class MarketingAmountStatisticsDTO implements Serializable {

	private static final long serialVersionUID = 1993943244684613208L;

	/**
	 * 回报率
	 */
	@ColumnWidth(30)
	@ExcelProperty("回报率")
	@Schema(description = "回报率")
	private BigDecimal returnOnInvestment;

	/**
	 * 商家营销支付金额
	 */
	@ColumnWidth(30)
	@ExcelProperty("营销支付金额")
	@Schema(description = "营销支付金额")
	private BigDecimal marketingPaymentAmount;

	/**
	 * 商家营销利润金额
	 *
	 * @return
	 */
	@ColumnWidth(30)
	@ExcelProperty("营销利润金额")
	@Schema(description = "营销利润金额")
	private BigDecimal marketingProfitAmount;

	/**
	 * 商家成交金额
	 */
	@ColumnWidth(30)
	@ExcelProperty("成交金额")
	@Schema(description = "成交金额")
	private BigDecimal transactionAmount;

	/**
	 * 平台累计下单成交数
	 */
	@ColumnWidth(30)
	@ExcelProperty("成交订单数")
	@Schema(description = "成交订单数")
	private Long payCount;

	/**
	 * 成交商品数
	 */
	@ColumnWidth(30)
	@ExcelProperty("成交商品数")
	@Schema(description = "成交商品数")
	private Long dealCount;

	/**
	 * 成交用户数
	 */
	@ExcelIgnore
	@Schema(description = "成交用户数")
	private Long userPayCount;

	public MarketingAmountStatisticsDTO() {
		this.marketingProfitAmount = BigDecimal.ZERO;
		this.transactionAmount = BigDecimal.ZERO;
		this.marketingPaymentAmount = BigDecimal.ZERO;
		this.returnOnInvestment = BigDecimal.ZERO;
		this.payCount = 0L;
		this.dealCount = 0L;
		this.userPayCount = 0L;
	}


}
