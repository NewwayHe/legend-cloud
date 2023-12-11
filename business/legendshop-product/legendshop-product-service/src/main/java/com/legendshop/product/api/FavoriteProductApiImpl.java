/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.product.dto.FavoriteProductDTO;
import com.legendshop.product.service.FavoriteProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Feign调用实现
 *
 * @author legendshop
 */
@RestController
@Validated
@RequiredArgsConstructor
public class FavoriteProductApiImpl implements FavoriteProductApi {

	final FavoriteProductService favoriteProductService;

	@Override
	public R<List<Long>> queryProductIdByUserId(Long id) {
		return R.ok(favoriteProductService.queryProductIdByUserId(id));
	}

	@Override
	public R<Integer> userFavoriteCount(Long id) {
		return favoriteProductService.userFavoriteCount(id);
	}

	@Override
	public R<List<FavoriteProductDTO>> getFavouriteProductId(Long userId, List<Long> productIdList) {
		return favoriteProductService.getFavouriteProductId(userId, productIdList);
	}
}
