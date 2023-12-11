/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.PageParams;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.shop.dao.FavoriteShopDao;
import com.legendshop.shop.dto.FavoriteShopDTO;
import com.legendshop.shop.entity.FavoriteShop;
import com.legendshop.shop.query.ShopFavoriteQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 店铺收藏Dao.
 *
 * @author legendshop
 */
@Repository
public class FavoriteShopDaoImpl extends GenericDaoImpl<FavoriteShop, Long> implements FavoriteShopDao {

	@Override
	public boolean deleteByUserId(Long id, Long userId) {
		return update("delete from ls_favorite_shop where user_id = ? and fs_id = ?  ", userId, id) > 0;
	}

	@Override
	public boolean isExists(Long userId, Long shopId) {
		Long counts = getLongResult("SELECT COUNT(lfs.id) FROM ls_favorite_shop lfs WHERE lfs.user_id = ? AND lfs.shop_id = ?", userId,
				shopId);
		return counts > 0;
	}

	@Override
	public void deleteByUserIdByIds(Long userId, List<Long> idList) {
		List<Object[]> batchArgs = new ArrayList<Object[]>(idList.size());
		for (Long id : idList) {
			Object[] param = new Object[2];
			param[0] = userId;
			param[1] = id;
			batchArgs.add(param);
		}
		this.batchUpdate("delete from ls_favorite_shop where user_id = ? and fs_id = ?", batchArgs);

	}

	@Override
	public void deleteAllByUserId(Long userId) {
		String sql = "delete from ls_favorite_shop where user_id = ?";
		this.update(sql, userId);
	}

	@Override
	public int deleteByShopIdAndUserId(Long shopId, Long userId) {
		return this.update("DELETE FROM ls_favorite_shop WHERE shop_id = ? AND user_id = ?", shopId, userId);
	}

	@Override
	public int deleteByShopIdAndUserId(List<Long> shopIds, Long userId) {
		if (CollUtil.isEmpty(shopIds)) {
			return 0;
		}

		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ls_favorite_shop WHERE user_id = ? AND shop_id in (");
		for (Long shopId : shopIds) {
			sql.append(shopId);
			sql.append(",");
		}
		sql.setLength(sql.length() - 1);
		sql.append(")");
		return update(sql.toString(), userId);
	}

	@Override
	public Integer userFavoriteCount(Long userId) {
		return this.get("select count(1) from ls_favorite_shop lsf,ls_shop_detail lsd where lsf.shop_id = lsd.id and lsf.user_id = ? ", Integer.class, userId);
	}


	@Override
	public PageSupport<FavoriteShop> querySimplePage(PageParams pageParams, Long userId) {
		SimpleSqlQuery query = new SimpleSqlQuery(FavoriteShop.class, pageParams.getPageSize(), pageParams.getCurPage());
		QueryMap map = new QueryMap();
		map.put("userId", userId);
		query.setSqlAndParameter("FavouriteShop.uc.queryFavouriteShop", map);
		return querySimplePage(query);
	}

	@Override
	public void deletefavoriteShops(Long[] fsIds, Long userId) {
		List<Object[]> batchArgs = new ArrayList<Object[]>(fsIds.length);
		for (Long fsId : fsIds) {
			Object[] param = new Object[2];
			param[0] = fsId;
			param[1] = userId;
			batchArgs.add(param);
		}
		this.batchUpdate("DELETE FROM ls_favorite_shop WHERE id = ? AND user_id = ?", batchArgs);
	}


	@Override
	public PageSupport<FavoriteShopDTO> queryPage(ShopFavoriteQuery favoriteShopQuery) {

		SimpleSqlQuery query = new SimpleSqlQuery(FavoriteShopDTO.class, favoriteShopQuery.getPageSize(), favoriteShopQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("userId", favoriteShopQuery.getUserId());
		if (ObjectUtil.isNotEmpty(favoriteShopQuery.getStartDate())) {
			map.put("startDate", DateUtil.beginOfDay(favoriteShopQuery.getStartDate()));
		}
		if (ObjectUtil.isNotEmpty(favoriteShopQuery.getEndDate())) {
			map.put("endDate", DateUtil.endOfDay(favoriteShopQuery.getEndDate()));
		}
		if (StrUtil.isNotBlank(favoriteShopQuery.getShopName())) {
			map.put("shopName", "%" + favoriteShopQuery.getShopName() + "%");
		}
		String queryAllSql = getSQL("FavouriteShop.queryFavouriteShopCount", map);
		String querySql = getSQL("FavouriteShop.queryFavouriteShop", map);
		query.setAllCountString(queryAllSql);
		query.setQueryString(querySql);
		query.setParam(map.toArray());
		return querySimplePage(query);
	}

	@Override
	public Long getCollectCountByShopId(Long shopId) {
		String sql = "SELECT count(*) FROM ls_favorite_shop WHERE shop_id = ?";
		return getLongResult(sql, shopId);
	}
}
