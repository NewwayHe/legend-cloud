/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 店铺营销数据报表
 *
 * @author legendshop
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "营销金额数据统计")
public class MerchantStatementCountDTO implements Serializable {

	/**
	 * 店铺新增成交金额
	 */
	@Schema(description = "店铺新增成交金额")
	private BigDecimal merchantNewTransactionAmount;

	/**
	 * 店铺累计成交金额
	 */
	@Schema(description = "店铺累计成交金额")
	private BigDecimal merchantTransactionAmountList;

	/**
	 * 店铺新增成交订单数
	 */
	@Schema(description = "店铺新增成交订单数")
	private int merchantNewAddOrderCount;

	/**
	 * 店铺累计成交订单数
	 */
	@Schema(description = "店铺累计成交订单数")
	private int merchantOrderCountList;

	/**
	 * 店铺新增成交商品数量
	 */
	@Schema(description = "店铺新增成交商品数量")
	private int mMerchantNewAddDealCount;

	/**
	 * 店铺累计成交商品数量
	 */
	@Schema(description = "店铺累计成交商品数量")
	private int merchantDealCount;

	/**
	 * 店铺新增下单用户数
	 */
	@Schema(description = "店铺新增下单用户数")
	private int merchantNewOrderUserCount;

	/**
	 * 店铺累计下单用户数
	 */
	@Schema(description = "店铺累计下单用户数")
	private int merchantOrderUserCountList;
	/**
	 * 店铺优惠券领取次数
	 */
	@Schema(description = "店铺优惠券领取次数和用户")
	private int merchantCouponsReceivedCount;


	/**
	 * 店铺下单老用户
	 */
	@Schema(description = "店铺下单老用户")
	private int merchantOrderOldUserCount;

	/**
	 * 店铺下单新用户
	 */
	@Schema(description = "店铺下单新用户")
	private int merchantOrderNewUserCount;

	/**
	 * 店铺回报率
	 */
	@Schema(description = "回报率")
	private BigDecimal merchantreturnOnInvestment;

	/**
	 * 商家累计下单成交数
	 */
	@Schema(description = "商家累计下单成交数")
	private Integer merchantPayCount;

	/**
	 * 商家下单成交用户数
	 */
	@Schema(description = "商家用户成交用户数")
	private int merchantUserPayCount;

	/**
	 * 商家下单用户数
	 */
	@Schema(description = "商家下单用户数")
	private int merchantUserOrderCount;

	/**
	 * 商家下单数
	 */
	@Schema(description = "商家下单数")
	private int merchantOrderCount;
	/**
	 * 商家营销利润金额
	 *
	 * @return
	 */
	@Schema(description = "商家营销利润金额")
	private BigDecimal merchantMarketingAmount;

	/**
	 * 商家成交金额
	 */
	@Schema(description = "商家成交金额")
	private BigDecimal merchantTransactionAmount;

	/**
	 * 商家营销支付金额
	 */
	@Schema(description = "商家营销支付金额")
	private BigDecimal merchantMarketingPaymentAmount;

	/**
	 * 回报率
	 */
	@Schema(description = "回报率")
	private BigDecimal returnOnInvestment;
}
