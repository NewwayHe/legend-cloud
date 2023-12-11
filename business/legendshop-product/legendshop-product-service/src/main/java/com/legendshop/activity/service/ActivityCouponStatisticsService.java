/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service;


import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.activity.dto.ActivityCouponStatisticsDTO;
import com.legendshop.activity.query.ActivityCouponStatisticsQuery;

import java.util.List;

/**
 * 活动列表优惠卷
 *
 * @author legendshop
 */
public interface ActivityCouponStatisticsService {
	/**
	 * 活动列表商家优惠卷
	 *
	 * @param query 领券时间参数
	 * @return
	 */
	PageSupport<ActivityCouponStatisticsDTO> pageActivityShopCoupon(ActivityCouponStatisticsQuery query);

	/**
	 * 活动列表平台优惠卷
	 *
	 * @param query 领券时间参数
	 * @return
	 */
	PageSupport<ActivityCouponStatisticsDTO> pageActivityPlatformCoupon(ActivityCouponStatisticsQuery query);

	/**
	 * 平台优惠卷导出
	 *
	 * @param query 领券时间参数
	 * @return
	 */
	List<ActivityCouponStatisticsDTO> getFlowExcelPlatform(ActivityCouponStatisticsQuery query);

	/**
	 * 商家优惠卷导出
	 *
	 * @param query 领券时间参数
	 * @return
	 */
	List<ActivityCouponStatisticsDTO> getFlowExcelShop(ActivityCouponStatisticsQuery query);
}
