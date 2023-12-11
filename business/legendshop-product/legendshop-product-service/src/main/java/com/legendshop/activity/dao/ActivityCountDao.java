/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.activity.dto.ActivityAnalysisDTO;
import com.legendshop.activity.dto.ActivityUsageCountDTO;
import com.legendshop.activity.enums.CouponProviderEnum;
import com.legendshop.activity.enums.MarketingTypeEnum;
import com.legendshop.activity.query.MarketingDataViewQuery;
import com.legendshop.common.datasource.NonTable;

import java.util.List;

/**
 * @author legendshop
 */
public interface ActivityCountDao extends GenericDao<NonTable, Long> {


	/**------------------------------------------------新增使用次数-----------------------------------------------------------------------------------*/
	List<ActivityUsageCountDTO> marketingRewardUsageCount(MarketingDataViewQuery viewQuery, Integer type);

	List<ActivityUsageCountDTO> marketingLimitUsageCount(MarketingDataViewQuery viewQuery, Boolean boot);

	List<ActivityUsageCountDTO> marketingCouponCount(MarketingDataViewQuery viewQuery, CouponProviderEnum shopCoupon, Boolean boot);

	/**-------------------------------------------------新增成交金额----------------------------------------------------------------------------------*/
	List<ActivityUsageCountDTO> marketingRewardDealCount(MarketingDataViewQuery viewQuery, Integer type);

	List<ActivityUsageCountDTO> marketingLimitDealCount(MarketingDataViewQuery viewQuery, Boolean boot);

	List<ActivityUsageCountDTO> marketingCouponDealCount(MarketingDataViewQuery viewQuery, CouponProviderEnum coupon, Boolean boot);

	List<ActivityUsageCountDTO> activityDealCount(MarketingDataViewQuery viewQuery, MarketingTypeEnum activity, Boolean boot);

	/*********************************************商家端*************************************************************************************************/

	List<ActivityAnalysisDTO> activitypayCount(MarketingDataViewQuery viewQuery);

	ActivityAnalysisDTO activityDealRate(MarketingDataViewQuery viewQuery);


}
