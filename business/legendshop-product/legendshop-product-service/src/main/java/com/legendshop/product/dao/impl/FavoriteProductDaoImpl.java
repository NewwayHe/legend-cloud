/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.product.dao.FavoriteProductDao;
import com.legendshop.product.dto.FavoriteProductDTO;
import com.legendshop.product.entity.FavoriteProduct;
import com.legendshop.product.query.ProductQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的商品收藏Dao
 *
 * @author legendshop
 */
@Repository
public class FavoriteProductDaoImpl extends GenericDaoImpl<FavoriteProduct, Long> implements FavoriteProductDao {

	@Override
	public boolean isExistsFavorite(Long productId, Long userId) {
		Long counts = getLongResult("select count(*) from ls_favorite_product where product_id = ? and user_id = ?", productId, userId);
		return counts > 0;
	}

	@Override
	public void deleteFavorite(Long userId, Long[] favIds) {
		List<Object[]> batchArgs = new ArrayList<Object[]>(favIds.length);
		for (Long favId : favIds) {
			Object[] param = new Object[2];
			param[0] = userId;
			param[1] = favId;
			batchArgs.add(param);
		}
		this.batchUpdate("delete from ls_favorite_product where user_id = ? and id = ?", batchArgs);

	}

	/**
	 * 用户端的查询收藏的商品
	 *
	 * @return
	 */
	@Override
	public PageSupport<FavoriteProductDTO> collect(ProductQuery productQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(FavoriteProductDTO.class, productQuery.getPageSize(), productQuery.getCurPage());
		QueryMap queryMap = new QueryMap();
		queryMap.put("userId", productQuery.getUserId());
		if (ObjectUtil.isNotEmpty(productQuery.getStartDate())) {
			queryMap.put("startDate", DateUtil.beginOfDay(productQuery.getStartDate()));
		}
		if (ObjectUtil.isNotEmpty(productQuery.getEndDate())) {
			queryMap.put("endDate", DateUtil.endOfDay(productQuery.getEndDate()));
		}
		if (StrUtil.isNotBlank(productQuery.getName())) {
			queryMap.put("name", "%" + productQuery.getName() + "%");
		}
		query.setSqlAndParameter("FavoriteProduct.collect", queryMap);
		return querySimplePage(query);
	}


	@Override
	public int deleteFavoriteByProductIdAndUserId(Long productId, Long userId) {
		return update("DELETE FROM ls_favorite_product WHERE product_id = ? AND user_id = ?", productId, userId);
	}

	@Override
	public int deleteFavoriteByProductIdAndUserId(List<Long> productIds, Long userId) {
		if (CollUtil.isEmpty(productIds)) {
			return 0;
		}

		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ls_favorite_product WHERE user_id = ? AND product_id in (");
		for (Long productId : productIds) {
			sql.append(productId);
			sql.append(",");
		}
		sql.setLength(sql.length() - 1);
		sql.append(")");
		return update(sql.toString(), userId);
	}

	@Override
	public List<Long> queryProductIdByUserId(Long userId) {
		return query("SELECT product_id from ls_favorite_product f where f.user_id=?", Long.class, userId);
	}

	@Override
	public Integer userFavoriteCount(Long userId) {
		return get("SELECT COUNT(*) FROM ls_favorite_product WHERE user_id = ?", Integer.class, userId);
	}

	@Override
	public List<FavoriteProductDTO> getFavouriteProductId(Long userId, List<Long> productIdList) {
		if (CollUtil.isEmpty(productIdList)) {
			return null;
		}
		List<Object> param = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select * FROM ls_favorite_product WHERE user_id = ? AND product_id in (");
		for (Long productId : productIdList) {
			sql.append("?");
			sql.append(",");
		}
		sql.setLength(sql.length() - 1);
		sql.append(")");
		param.add(userId);
		param.addAll(productIdList);
		return query(sql.toString(), FavoriteProductDTO.class, param.toArray());
	}
}
