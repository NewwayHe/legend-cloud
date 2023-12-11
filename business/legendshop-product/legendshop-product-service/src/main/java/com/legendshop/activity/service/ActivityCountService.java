/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service;

import com.legendshop.activity.dto.*;
import com.legendshop.activity.query.MarketingDataViewQuery;

import java.util.List;

/**
 * @author legendshop
 */
public interface ActivityCountService {

	List<ActivityUsageCountDTO> getMasketingDataPage(MarketingDataViewQuery viewQuery);

	List<MarketingDataViewLineDTO> getMasketingViewLine(MarketingDataViewQuery viewQuery);

	List<MarketingDataDealLineDTO> getMasketingDealLine(MarketingDataViewQuery viewQuery);

	List<ActivityShopUsageDTO> getMasketingTotalCount(MarketingDataViewQuery viewQuery);

	MasketingAnalysisDTO getMasketingAnalysis(MarketingDataViewQuery viewQuery);
}
