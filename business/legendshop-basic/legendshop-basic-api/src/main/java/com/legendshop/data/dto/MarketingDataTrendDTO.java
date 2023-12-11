/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketingDataTrendDTO implements Serializable {

	/**
	 * 查询时间
	 */
	@Schema(description = "时间")
	private Date time;

	/**
	 * 店铺新增成交金额
	 */
	@Schema(description = "新增成交金额")
	BigDecimal newTransactionAmount;

	/**
	 * 店铺累计成交金额
	 */
	@Schema(description = "累计成交金额")
	BigDecimal transactionAmountList;

	/**
	 * 店铺新增成交订单数
	 */
	@Schema(description = "新增成交订单数")
	BigDecimal newAddOrderCount;

	/**
	 * 店铺累计成交订单数
	 */
	@Schema(description = "累计成交订单数")
	BigDecimal orderCountList;

	/**
	 * 店铺新增成交商品数量
	 */
	@Schema(description = "新增成交商品数量")
	BigDecimal newAddDealCount;

	/**
	 * 店铺累计成交商品数量
	 */
	@Schema(description = "累计成交商品数量")
	BigDecimal dealCount;

	/**
	 * 店铺新增下单用户数
	 */
	@Schema(description = "新增下单用户数")
	BigDecimal newOrderUserCount;

	/**
	 * 店铺累计下单用户数
	 */
	@Schema(description = "累计下单用户数")
	BigDecimal orderUserCountList;
}
