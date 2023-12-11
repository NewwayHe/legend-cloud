/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service;

import com.legendshop.activity.dto.MarketingActivityItemDTO;

import java.util.List;
import java.util.Map;

/**
 * @author legendshop
 */
public interface MarketingActivityService {


	/**
	 * 根据商品id获取获取下面所有sku对应的促销信息和优惠劵
	 *
	 * @param productId
	 * @return
	 */
	Map<Long, List<MarketingActivityItemDTO>> getMarketingActivity(Long productId, Long userId);
}
