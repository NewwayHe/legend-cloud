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
import cn.legendshop.jpaplus.support.PageParams;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.shop.dto.FavoriteShopDTO;
import com.legendshop.shop.entity.FavoriteShop;
import com.legendshop.shop.query.ShopFavoriteQuery;

import java.util.List;

/**
 * 店铺收藏Dao.
 *
 * @author legendshop
 */
public interface FavoriteShopDao extends GenericDao<FavoriteShop, Long> {

	/**
	 * 根据用户ID删除店铺收藏记录.
	 *
	 * @param id     店铺收藏记录ID
	 * @param userId 用户ID
	 * @return 是否删除成功
	 */
	boolean deleteByUserId(Long id, Long userId);

	/**
	 * 判断店铺是否存在于用户的收藏列表中.
	 *
	 * @param userId 用户ID
	 * @param shopId 店铺ID
	 * @return 是否存在
	 */
	boolean isExists(Long userId, Long shopId);

	/**
	 * 根据用户ID和店铺收藏ID列表批量删除记录.
	 *
	 * @param userId 用户ID
	 * @param idList 店铺收藏ID列表
	 */
	void deleteByUserIdByIds(Long userId, List<Long> idList);

	/**
	 * 根据用户ID删除所有店铺收藏记录.
	 *
	 * @param userId 用户ID
	 */
	void deleteAllByUserId(Long userId);

	/**
	 * 根据店铺ID和用户ID删除店铺收藏记录.
	 *
	 * @param shopId 店铺ID
	 * @param userId 用户ID
	 * @return 删除的记录数量
	 */
	int deleteByShopIdAndUserId(Long shopId, Long userId);

	/**
	 * 根据店铺ID列表和用户ID删除店铺收藏记录.
	 *
	 * @param shopIds 店铺ID列表
	 * @param userId  用户ID
	 * @return 删除的记录数量
	 */
	int deleteByShopIdAndUserId(List<Long> shopIds, Long userId);

	/**
	 * 获取收藏的店铺数量.
	 *
	 * @param userId 用户ID
	 * @return 店铺收藏数量
	 */
	Integer userFavoriteCount(Long userId);

	/**
	 * 分页查询用户收藏的店铺简要信息.
	 *
	 * @param pageParams 分页参数
	 * @param userId     用户ID
	 * @return 分页结果集
	 */
	PageSupport<FavoriteShop> querySimplePage(PageParams pageParams, Long userId);

	/**
	 * 根据用户ID和收藏店铺ID数组批量删除记录.
	 *
	 * @param favIds 收藏店铺ID数组
	 * @param userId 用户ID
	 */
	void deletefavoriteShops(Long[] favIds, Long userId);

	/**
	 * 分页查询收藏的店铺列表.
	 *
	 * @param shopFavoriteQuery 店铺收藏查询对象
	 * @return 分页结果集
	 */
	PageSupport<FavoriteShopDTO> queryPage(ShopFavoriteQuery shopFavoriteQuery);

	/**
	 * 查询店铺被收藏的数量.
	 *
	 * @param shopId 店铺ID
	 * @return 店铺被收藏的数量
	 */
	Long getCollectCountByShopId(Long shopId);
}
