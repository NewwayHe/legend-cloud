/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import com.legendshop.product.dao.ProductCommentStatisticsDao;
import com.legendshop.product.entity.ProductCommentStatistics;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import static com.legendshop.common.core.constant.CacheConstants.COMMENT_SCORE;
import static com.legendshop.common.core.constant.CacheConstants.PRODUCT_STATISTICS;

/**
 * 商品评论统计表
 *
 * @author legendshop
 */
@Repository
public class ProductCommentStatisticsDaoImpl extends GenericDaoImpl<ProductCommentStatistics, Long> implements ProductCommentStatisticsDao {


	@Override
	public Long saveProductCommentStat(ProductCommentStatistics productCommentStat) {
		return save(productCommentStat);
	}

	@Override
	public int updateProductCommentStat(Integer score, Integer count, Long productId) {
		String sql = "update ls_product_comment_statistics set score = score+?,comments = comments+? where product_id=?";
		return update(sql, score, count, productId);
	}


	@Override
	public ProductCommentStatistics getProductCommentStatByProductIdForUpdate(Long productId) {
		String sql = "select * from ls_product_comment_statistics where product_id = ? for update";
		return this.get(sql, ProductCommentStatistics.class, productId);
	}

	@Override
	@Cacheable(value = COMMENT_SCORE + PRODUCT_STATISTICS, key = "#shopId", unless = "#result == null ")
	public ProductCommentStatistics getProductCommentStatByShopId(Long shopId) {
		String sql = "SELECT  SUM(s.score) AS score,SUM(s.comments) AS comments FROM ls_product_comment_statistics s, ls_product p WHERE s.product_id = p.id AND p.shop_id = ?";
		return this.get(sql, ProductCommentStatistics.class, shopId);
	}


}
