/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.product.dto.BatchFavoriteProductDTO;
import com.legendshop.product.dto.FavoriteProductDTO;
import com.legendshop.product.query.ProductQuery;

import java.util.List;

/**
 * 我的商品收藏
 *
 * @author legendshop
 */
public interface FavoriteProductService {

	void deleteFavs(Long userId, Long[] selectedFavs);

	/**
	 * 收集我的商品收藏, 带有收藏的商品总数
	 */
	PageSupport<FavoriteProductDTO> collect(ProductQuery query);

	/**
	 * 判断用户是都收藏了该商品
	 *
	 * @param productId
	 * @param userId
	 * @return
	 */
	boolean isExistsFavorite(Long productId, Long userId);

	/**
	 * 新增收藏和取消收藏
	 *
	 * @return
	 */
	boolean favoriteFlag(BatchFavoriteProductDTO favoriteProductDTO);

	/**
	 * 根据用户ID查询收藏的所有商品id
	 *
	 * @param userId
	 * @return
	 */
	List<Long> queryProductIdByUserId(Long userId);

	R<Integer> userFavoriteCount(Long userId);

	/**
	 * 查询当前用户是否收藏，0没收藏，非0为收藏id
	 *
	 * @param userId
	 * @param productIdList
	 * @return
	 */
	R<List<FavoriteProductDTO>> getFavouriteProductId(Long userId, List<Long> productIdList);
}
