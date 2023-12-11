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
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.dto.FavoriteProductDTO;
import com.legendshop.product.entity.FavoriteProduct;
import com.legendshop.product.query.ProductQuery;

import java.util.List;

/**
 * 我的商品收藏Dao
 *
 * @author legendshop
 */
public interface FavoriteProductDao extends GenericDao<FavoriteProduct, Long> {

	/**
	 * 根据收藏ID数组删除用户的收藏记录。
	 *
	 * @param userId 用户ID
	 * @param favIds 收藏ID数组
	 */
	void deleteFavorite(Long userId, Long[] favIds);

	/**
	 * 根据商品ID和用户ID删除收藏记录。
	 *
	 * @param productId 商品ID
	 * @param userId    用户ID
	 * @return 删除操作的影响行数
	 */
	int deleteFavoriteByProductIdAndUserId(Long productId, Long userId);

	/**
	 * 根据商品ID列表和用户ID删除收藏记录。
	 *
	 * @param productIds 商品ID列表
	 * @param userId     用户ID
	 * @return 删除操作的影响行数
	 */
	int deleteFavoriteByProductIdAndUserId(List<Long> productIds, Long userId);

	/**
	 * 检查指定商品和用户是否存在收藏记录。
	 *
	 * @param productId 商品ID
	 * @param userId    用户ID
	 * @return 若存在收藏记录则返回true，否则返回false
	 */
	boolean isExistsFavorite(Long productId, Long userId);
	/**
	 * 收集我的商品收藏的方法。
	 *
	 * @param productQuery 商品查询对象
	 * @return 我的商品收藏的分页支持对象
	 */
	PageSupport<FavoriteProductDTO> collect(ProductQuery productQuery);


	/**
	 * 根据用户id查询所有收藏的商品
	 *
	 * @param userId
	 * @return
	 */
	List<Long> queryProductIdByUserId(Long userId);

	/**
	 * 获取用户收藏的商品数量。
	 *
	 * @param userId 用户ID
	 * @return 用户收藏的商品数量
	 */
	Integer userFavoriteCount(Long userId);


	/**
	 * 查询当前用户是否收藏，0没收藏，非0为收藏id
	 *
	 * @param userId
	 * @param productIdList
	 * @return
	 */
	List<FavoriteProductDTO> getFavouriteProductId(Long userId, List<Long> productIdList);
}
