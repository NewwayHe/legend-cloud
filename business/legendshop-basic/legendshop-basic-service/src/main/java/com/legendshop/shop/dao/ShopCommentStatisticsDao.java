/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.shop.entity.ShopCommentStatistics;

/**
 * 店铺评分统计Dao
 *
 * @author legendshop
 */
public interface ShopCommentStatisticsDao extends GenericDao<ShopCommentStatistics, Long> {

	/**
	 * 计算店铺服务评分的接口
	 *
	 * @param shopId
	 * @return
	 */
	ShopCommentStatistics getShopCommStatByShopId(Long shopId);

	/**
	 * 获取店铺评分
	 *
	 * @param shopId
	 * @return
	 */
	ShopCommentStatistics getShopCommStatByShopIdForUpdate(Long shopId);

	/**
	 * 保存店铺评分统计
	 *
	 * @param shopCommStat
	 * @return
	 */
	Long saveShopCommStat(ShopCommentStatistics shopCommStat);

	/**
	 * 更新店铺评分统计
	 *
	 * @param score
	 * @param count
	 * @return
	 */
	int updateShopCommStat(Integer score, Integer count, Long shopId);

}
