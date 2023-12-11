/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import com.legendshop.shop.dao.ShopCommentStatisticsDao;
import com.legendshop.shop.entity.ShopCommentStatistics;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import static com.legendshop.common.core.constant.CacheConstants.COMMENT_SCORE;
import static com.legendshop.common.core.constant.CacheConstants.SHOP_STATISTICS;

/**
 * 店铺评分统计Dao
 *
 * @author legendshop
 */
@Repository
public class ShopCommentStatisticsDaoImpl extends GenericDaoImpl<ShopCommentStatistics, Long> implements ShopCommentStatisticsDao {

	@Override
	public ShopCommentStatistics getShopCommStatByShopIdForUpdate(Long shopId) {

		String sql = "SELECT * FROM ls_shop_comment_statistics WHERE shop_id = ? FOR UPDATE";

		return this.get(sql, ShopCommentStatistics.class, shopId);
	}

	@Override
	@Cacheable(value = COMMENT_SCORE + SHOP_STATISTICS, key = "#shopId", unless = "#result == null ")
	public ShopCommentStatistics getShopCommStatByShopId(Long shopId) {
		String sql = "SELECT  SUM(s.score) AS score,SUM(s.count) AS count FROM ls_shop_comment_statistics s WHERE s.shop_id = ?";
		return this.get(sql, ShopCommentStatistics.class, shopId);
	}

	@Override
	public Long saveShopCommStat(ShopCommentStatistics shopCommStat) {
		return save(shopCommStat);
	}

	@Override
	public int updateShopCommStat(Integer score, Integer count, Long shopId) {
		String sql = "update ls_shop_comment_statistics set score = score+?,count = count+? where shop_id=?";
		return update(sql, score, count, shopId);
	}

}
