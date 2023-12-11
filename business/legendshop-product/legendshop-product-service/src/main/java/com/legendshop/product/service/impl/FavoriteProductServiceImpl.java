/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.product.dao.FavoriteProductDao;
import com.legendshop.product.dto.BatchFavoriteProductDTO;
import com.legendshop.product.dto.FavoriteProductDTO;
import com.legendshop.product.entity.FavoriteProduct;
import com.legendshop.product.query.ProductQuery;
import com.legendshop.product.service.FavoriteProductService;
import com.legendshop.product.service.convert.FavoriteProductConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 我的商品收藏
 *
 * @author legendshop
 */
@Service
public class FavoriteProductServiceImpl implements FavoriteProductService {

	@Autowired
	private FavoriteProductDao favoriteProductDao;

	@Autowired
	private FavoriteProductConverter favoriteProductConverter;

	@Override
	public void deleteFavs(Long userId, Long[] selectedFavs) {
		favoriteProductDao.deleteFavorite(userId, selectedFavs);
	}

	@Override
	public PageSupport<FavoriteProductDTO> collect(ProductQuery query) {
		PageSupport<FavoriteProductDTO> page = favoriteProductDao.collect(query);
		return page;
	}


	@Override
	public boolean isExistsFavorite(Long productId, Long userId) {
		return favoriteProductDao.isExistsFavorite(productId, userId);
	}

	/**
	 * 收藏和取消收藏
	 *
	 * @param favoriteProductDTO
	 * @return
	 */
	@Override
	public boolean favoriteFlag(BatchFavoriteProductDTO favoriteProductDTO) {
		if (CollUtil.isEmpty(favoriteProductDTO.getProductIds())) {
			throw new BusinessException("商品ID不能为空");
		}
		if (favoriteProductDTO.getCollectionFlag()) {
			List<FavoriteProduct> favoriteProductList = new ArrayList<>();
			FavoriteProduct favoriteProduct = null;
			for (Long productId : favoriteProductDTO.getProductIds()) {
				favoriteProduct = new FavoriteProduct();
				favoriteProduct.setAddtime(DateUtil.date());
				favoriteProduct.setUserId(favoriteProductDTO.getUserId());
				favoriteProduct.setProductId(productId);
				favoriteProduct.setSource(favoriteProductDTO.getSource());
				favoriteProductList.add(favoriteProduct);
			}
			favoriteProductDao.save(favoriteProductList);
			return true;
		} else if (!favoriteProductDTO.getCollectionFlag()) {
			favoriteProductDao.deleteFavoriteByProductIdAndUserId(favoriteProductDTO.getProductIds(), favoriteProductDTO.getUserId());
			return true;
		}
		return false;
	}

	@Override
	public List<Long> queryProductIdByUserId(Long userId) {
		return favoriteProductDao.queryProductIdByUserId(userId);
	}

	@Override
	public R<Integer> userFavoriteCount(Long userId) {
		return R.ok(Optional.ofNullable(this.favoriteProductDao.userFavoriteCount(userId)).orElse(0));
	}

	@Override
	public R<List<FavoriteProductDTO>> getFavouriteProductId(Long userId, List<Long> productIdList) {
		return R.ok(favoriteProductDao.getFavouriteProductId(userId, productIdList));
	}
}
