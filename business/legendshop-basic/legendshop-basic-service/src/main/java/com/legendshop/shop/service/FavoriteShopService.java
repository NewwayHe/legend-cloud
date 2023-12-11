/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service;

import cn.legendshop.jpaplus.support.PageParams;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.shop.dto.BatchFavoriteShopDTO;
import com.legendshop.shop.dto.FavoriteShopDTO;
import com.legendshop.shop.query.ShopFavoriteQuery;

/**
 * 店铺收藏服务.
 *
 * @author legendshop
 */
public interface FavoriteShopService {

	FavoriteShopDTO getById(Long id);

	void deleteById(Long id);

	Long save(FavoriteShopDTO favoriteShop);

	void update(FavoriteShopDTO favoriteShop);

	Boolean deleteByUserId(Long id, Long userId);


	void deleteByUserIdByIds(Long userId, String ids);

	void deleteAllByUserId(Long userId);

	Integer deleteByShopIdAndUserId(Long shopId, Long userId);

	R<Integer> userFavoriteCount(Long userId);

	/**
	 * 判断是否已经收藏店铺
	 *
	 * @param userId
	 * @param shopId
	 * @return
	 */
	Boolean isExists(Long userId, Long shopId);

	/**
	 * 店铺收藏
	 *
	 * @param favoriteShopDTO
	 * @return
	 */
	Boolean favoriteShopFlag(BatchFavoriteShopDTO favoriteShopDTO);

	PageSupport<FavoriteShopDTO> query(PageParams pageParams, Long userId);

	/**
	 * 获取收藏的店铺列表
	 *
	 * @param shopFavoriteQuery
	 * @return
	 */
	PageSupport<FavoriteShopDTO> queryFavoriteShopPageList(ShopFavoriteQuery shopFavoriteQuery);

	/**
	 * 批量删除
	 *
	 * @param userId
	 * @param selectedFavs
	 */
	void deleteFavs(Long userId, Long[] selectedFavs);
}
