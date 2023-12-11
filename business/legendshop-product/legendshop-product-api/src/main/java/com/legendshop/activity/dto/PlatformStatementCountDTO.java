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
import java.util.Date;

/**
 * 平台营销数据报表dto
 *
 * @author legendshop
 */
@Data
@NoArgsConstructor
@AllArgsConstructor

public class PlatformStatementCountDTO implements Serializable {

	/**
	 * 日期
	 */
	@Schema(description = "日期")
	private Date date;

	/**
	 * 平台营销支付金额
	 */
	@Schema(description = "平台营销支付金额")
	private BigDecimal platformMarketingPaymentAmount;

	/**
	 * 平台成交金额
	 */
	@Schema(description = "平台成交金额")
	private BigDecimal platformTransactionAmount;

	/**
	 * 平台营销利润金额
	 */
	@Schema(description = "平台营销利润金额")
	private BigDecimal platformMarketingAmount;

	/**
	 * 平台下单用户数
	 */
	@Schema(description = "平台下单用户数")
	private int platformUserOrderCount;

	/**
	 * 平台下单数
	 */
	@Schema(description = "平台下单数")
	private int platformOrderCount;

	/**
	 * 平台用户下单成交数
	 */
	@Schema(description = "平台下单成交用户数")
	private int platformUserPayCount;

	/**
	 * 累计成交用户数
	 */
	@Schema(description = "累计成交用户数")
	private Integer dealUserNum;

	/**
	 * 累计访问次数
	 */
	@Schema(description = "累计访问次数")
	private Integer totalView;

	/**
	 * 累计访问人数
	 */
	@Schema(description = "累计访问人数")
	private Integer totalViewPeople;

	/**
	 * 转化率
	 */
	@Schema(description = "转化率")
	private Double inversionRate;

	/**
	 * 平台累计下单成交数
	 */
	@Schema(description = "平台累计下单成交数")
	private Integer platformPayCount;

	/**
	 * 平台回报率
	 */
	@Schema(description = "回报率")
	private BigDecimal returnOnInvestment;

	/**
	 * 平台优惠券领取次数和领取用户
	 */
	@Schema(description = "平台优惠券领取次数和领取用户")
	private int platformCouponsReceivedCount;

	/**
	 * 平台下单老用户
	 */
	@Schema(description = "平台下单老用户")
	private int platformOrderOldUserCount;

	/**
	 * 平台下单新用户
	 */
	@Schema(description = "平台下单新用户")
	private int platformOrderNewUserCount;

	/**
	 * 新增成交金额
	 */
	@Schema(description = "平台新增成交金额")
	private int platformNewTransactionAmount;

	/**
	 * 累计成交金额
	 */
	@Schema(description = "平台累计成交金额")
	private int platformTransactionAmountList;

	/**
	 * 新增成交订单数
	 */
	@Schema(description = "平台新增成交订单数")
	private int platformNewAddOrderDealCount;

	/**
	 * 累计成交订单数
	 */
	@Schema(description = "平台累计成交订单数")
	private int platformDealCountList;

	/**
	 * 新增成交商品数量
	 */
	@Schema(description = "平台新增成交商品数量")
	private int platformNewAddDealCount;

	/**
	 * 累计成交商品数量
	 */
	@Schema(description = "平台累计成交商品数量")
	private int platformDealCount;

	/**
	 * 新增下单用户数
	 */
	@Schema(description = "平台新增下单用户数")
	private int platformNewOrderUserCount;

	/**
	 * 累计下单用户数
	 */
	@Schema(description = "平台累计下单用户数")
	private int platformOrderUserCountList;


}
