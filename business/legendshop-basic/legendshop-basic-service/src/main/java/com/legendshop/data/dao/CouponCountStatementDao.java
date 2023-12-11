/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dao;

import com.legendshop.activity.dto.MarketingDataTrendDTO;
import com.legendshop.data.dto.ActivityCouponCountQuery;
import com.legendshop.data.dto.MarketingAmountStatisticsDTO;
import com.legendshop.data.dto.MarketingUserStatisticsDTO;

import java.util.List;

/**
 * 平台营销信息查询 查找所有营销信息 统计成报表
 *
 * @author legendshop
 */
public interface CouponCountStatementDao {


	/**
	 * 营销利润，营销支付金额
	 *
	 * @return
	 */
	MarketingAmountStatisticsDTO getMarketingAmount(ActivityCouponCountQuery query);

	/**
	 * 成交金额，成交订单，成交数量、成交用户数
	 */
	MarketingAmountStatisticsDTO getTransactionAmount(ActivityCouponCountQuery query);

	/**
	 * 领取次数、领取用户数
	 */
	MarketingUserStatisticsDTO getReceivedCount(ActivityCouponCountQuery query);

	/**
	 * 累计领取次数、累计领取用户数
	 */
	MarketingUserStatisticsDTO getTotalReceivedCount(ActivityCouponCountQuery query);

	/**
	 * 下单用户数、下单次数
	 */
	MarketingUserStatisticsDTO getOrderCount(ActivityCouponCountQuery query);

	/**
	 * 获取有效下单用户数（生成订单未售后完成的数量）
	 *
	 * @param query
	 * @return
	 */
	Long getOldOrderUserCount(ActivityCouponCountQuery query);

	/**
	 * 获取下单用户总数
	 *
	 * @param query
	 * @return
	 */
	Long getOrderUserCount(ActivityCouponCountQuery query);


	/**
	 * 新增访问记录
	 *
	 * @return
	 */
	List<MarketingDataTrendDTO> queryPlatformNewVisitCount(ActivityCouponCountQuery query);

	/**
	 * 累计访问记录
	 *
	 * @return
	 */
	List<MarketingDataTrendDTO> queryPlatformVisitCount(ActivityCouponCountQuery query);

	/**
	 * 平台下单用户数，平台下单订单数
	 *
	 * @return
	 */
	@Deprecated
	MarketingUserStatisticsDTO getPlatformUserOrderCount(ActivityCouponCountQuery query);

	/**
	 * 平台用户下单成交数，平台下单成交数
	 *
	 * @return
	 */
	@Deprecated
	MarketingUserStatisticsDTO getPlatformUserPayCount(ActivityCouponCountQuery query);

	/**
	 * 平台优惠券领取次数和用户
	 *
	 * @return
	 */
	@Deprecated
	MarketingUserStatisticsDTO getPlatformCouponsReceivedCount(ActivityCouponCountQuery query);

	/**
	 * 平台下单老用户id
	 *
	 * @return
	 */
	@Deprecated
	List<MarketingUserStatisticsDTO> queryPlatformOrderOldUserCount(ActivityCouponCountQuery query);

	/**
	 * 平台下单用户id
	 *
	 * @return
	 */
	@Deprecated
	List<MarketingUserStatisticsDTO> queryPlatformOrderUserIdCount(ActivityCouponCountQuery query);

	/**
	 * 新增成交金额，成交数量，成交订单
	 *
	 * @return
	 */
	List<MarketingDataTrendDTO> queryPlatformNewAmount(ActivityCouponCountQuery query);

	/**
	 * 累计成交金额
	 *
	 * @return
	 */
	List<MarketingDataTrendDTO> queryPlatformAmountList(ActivityCouponCountQuery query);

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

	/**
	 * 查询访问人数
	 */
	MarketingUserStatisticsDTO getPlatformVisitCount(ActivityCouponCountQuery query);

}
