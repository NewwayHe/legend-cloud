/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service;

import com.legendshop.shop.dto.ShopCommentStatisticsDTO;

/**
 * 店铺评分统计服务
 *
 * @author legendshop
 */
public interface ShopCommentStatisticsService {

	ShopCommentStatisticsDTO getById(Long id);

	ShopCommentStatisticsDTO getByShopId(Long shopId);

	int deleteById(Long id);

	Long save(ShopCommentStatisticsDTO shopCommStatDto);

	int update(ShopCommentStatisticsDTO shopCommStatDto);

	ShopCommentStatisticsDTO getShopCommStatByShopIdForUpdate(Long shopId);

	void saveShopCommStat(ShopCommentStatisticsDTO shopCommStat);

	void updateShopCommStat(Integer score, Integer count, Long shopId);
}
