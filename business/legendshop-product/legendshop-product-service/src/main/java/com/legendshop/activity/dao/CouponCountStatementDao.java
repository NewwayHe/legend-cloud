/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dao;

import com.legendshop.data.dto.ActivityCouponCountQuery;
import com.legendshop.data.dto.MarketingDataTrendDTO;
import com.legendshop.data.dto.MarketingUserStatisticsDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 平台营销信息查询 查找所有营销信息 统计成报表
 *
 * @author legendshop
 */
public interface CouponCountStatementDao {
	/**
	 * 平台营销支付金额
	 *
	 * @return
	 */
	BigDecimal getPlatformMarketingPaymentAmount(ActivityCouponCountQuery query);

	/**
	 * 平台成交金额
	 *
	 * @return
	 */
	BigDecimal getPlatformTransactionAmount(ActivityCouponCountQuery query);

	/**
	 * 平台营销利润金额
	 *
	 * @return
	 */
	BigDecimal getPlatformMarketingAmount(ActivityCouponCountQuery query);

	/**
	 * 平台下单用户数
	 *
	 * @return
	 */
	int getPlatformUserOrderCount(ActivityCouponCountQuery query);

	/**
	 * 平台下单订单数
	 *
	 * @return
	 */
	int getPlatformOrderCount(ActivityCouponCountQuery query);

	/**
	 * 平台用户下单成交数
	 *
	 * @return
	 */
	int getPlatformUserPayCount(ActivityCouponCountQuery query);

	/**
	 * 平台下单成交数
	 *
	 * @return
	 */
	int getPlatformPayCount(ActivityCouponCountQuery query);

	/**
	 * 平台优惠券领取次数和用户
	 *
	 * @return
	 */
	MarketingUserStatisticsDTO getPlatformCouponsReceivedCount(ActivityCouponCountQuery query);

	/**
	 * 平台下单老用户id
	 *
	 * @return
	 */
	List<MarketingUserStatisticsDTO> queryPlatformOrderOldUserCount(ActivityCouponCountQuery query);

	/**
	 * 平台下单用户id
	 *
	 * @return
	 */
	List<MarketingUserStatisticsDTO> queryPlatformOrderUserIdCount(ActivityCouponCountQuery query);

	/**
	 * 新增成交金额
	 *
	 * @return
	 */
	List<MarketingDataTrendDTO> queryPlatformNewTransactionAmount(ActivityCouponCountQuery query);

	/**
	 * 累计成交金额
	 *
	 * @return
	 */
	List<MarketingDataTrendDTO> queryPlatformTransactionAmountList(ActivityCouponCountQuery query);

	/**
	 * 新增成交订单数
	 *
	 * @return
	 */
	List<MarketingDataTrendDTO> queryPlatformNewAddOrderDealCount(ActivityCouponCountQuery query);

	/**
	 * 累计成交订单数
	 *
	 * @return
	 */
	List<MarketingDataTrendDTO> queryPlatformDealCountList(ActivityCouponCountQuery query);

	/**
	 * 新增成交商品数量
	 *
	 * @return
	 */
	List<MarketingDataTrendDTO> queryPlatformNewAddDealCount(ActivityCouponCountQuery query);

	/**
	 * 累计成交商品数量
	 *
	 * @return
	 */
	List<MarketingDataTrendDTO> queryPlatformDealCount(ActivityCouponCountQuery query);

	/**
	 * 新增下单用户数
	 */
	List<MarketingDataTrendDTO> queryPlatformNewOrderUserCount(ActivityCouponCountQuery query);

	/**
	 * 累计下单用户数
	 *
	 * @return
	 */
	List<MarketingDataTrendDTO> queryPlatformOrderUserCountList(ActivityCouponCountQuery query);

}
