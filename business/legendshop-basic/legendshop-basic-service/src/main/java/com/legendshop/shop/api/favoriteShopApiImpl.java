/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.shop.service.FavoriteShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * 收藏店铺控制器
 *
 * @author legendshop
 */
@RestController
@RequiredArgsConstructor
public class favoriteShopApiImpl implements FavoriteShopApi {

	private final FavoriteShopService favoriteShopService;

	@Override
	public R<Integer> userFavoriteCount(Long userId) {
		return favoriteShopService.userFavoriteCount(userId);
	}

}
