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
import com.legendshop.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 店铺收藏服务
 *
 * @author legendshop
 */
@FeignClient(contextId = "favoriteShopApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface FavoriteShopApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	/**
	 * 用户收藏数量
	 *
	 * @param userId
	 * @return
	 */
	@PostMapping(PREFIX + "/p/favorite/shop/userFavoriteCount")
	R<Integer> userFavoriteCount(@RequestParam(value = "userId") Long userId);
}
