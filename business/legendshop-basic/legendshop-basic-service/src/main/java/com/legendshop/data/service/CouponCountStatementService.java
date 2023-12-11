/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.service;


import com.legendshop.activity.dto.MarketingDataTrendDTO;
import com.legendshop.data.dto.ActivityCouponCountQuery;
import com.legendshop.data.dto.MarketingAmountStatisticsDTO;
import com.legendshop.data.dto.MarketingUserStatisticsDTO;

import java.util.List;

/**
 * 营销信息查询 查找所有营销信息 统计成报表
 *
 * @author legendshop
 */
public interface CouponCountStatementService {

	/**
	 * 平台营销金额数据统计
	 *
	 * @return
	 */
	MarketingAmountStatisticsDTO marketingAmountStatistics(ActivityCouponCountQuery query);

	/**
	 * 平台营销用户数据统计
	 *
	 * @return
	 */
	MarketingUserStatisticsDTO marketingUserStatistics(ActivityCouponCountQuery query);

	/**
	 * 平台营销数据趋势统计
	 *
	 * @return
	 */
	List<MarketingDataTrendDTO> marketingDataTrendStatistics(ActivityCouponCountQuery query);

}
