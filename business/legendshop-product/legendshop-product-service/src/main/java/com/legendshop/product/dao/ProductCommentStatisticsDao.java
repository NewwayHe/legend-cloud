/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.product.entity.ProductCommentStatistics;

/**
 * 评论统计Dao.
 *
 * @author legendshop
 */
public interface ProductCommentStatisticsDao extends GenericDao<ProductCommentStatistics, Long> {

	/**
	 * 保存评论统计
	 *
	 * @param productCommentStat
	 * @return
	 */
	Long saveProductCommentStat(ProductCommentStatistics productCommentStat);

	/**
	 * 更新评论统计
	 *
	 * @param score
	 * @param comments
	 * @param productId
	 * @return
	 */
	int updateProductCommentStat(Integer score, Integer comments, Long productId);


	/**
	 * 根据店铺ID获取商品评论统计信息的方法。
	 *
	 * @param shopId 店铺ID
	 * @return 商品评论统计信息
	 */
	ProductCommentStatistics getProductCommentStatByShopId(Long shopId);

	/**
	 * 获取商品评分
	 *
	 * @param productId
	 * @return
	 */
	ProductCommentStatistics getProductCommentStatByProductIdForUpdate(Long productId);


}
