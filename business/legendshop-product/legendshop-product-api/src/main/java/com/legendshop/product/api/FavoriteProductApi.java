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
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.product.dto.FavoriteProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "favoriteProductApi", value = ServiceNameConstants.PRODUCT_SERVICE)
public interface FavoriteProductApi {

	String PREFIX = ServiceNameConstants.PRODUCT_SERVICE_RPC_PREFIX;
	/**
	 * 根据用户 ID 查询收藏商品的商品 ID 列表。
	 *
	 * @param id 用户 ID
	 * @return 包含收藏商品的商品 ID 列表
	 */
	@GetMapping(PREFIX + "/favoriteProduct/queryProductIdByUserId/{id}")
	R<List<Long>> queryProductIdByUserId(@PathVariable("id") Long id);

	/**
	 * 获取用户收藏的商品数量。
	 *
	 * @param id 用户 ID
	 * @return 用户收藏的商品数量
	 */
	@GetMapping(PREFIX + "/favoriteProduct/userFavoriteCount/{id}")
	R<Integer> userFavoriteCount(@PathVariable("id") Long id);

	/**
	 * 获取用户收藏的指定商品 ID 列表。
	 *
	 * @param userId       用户 ID
	 * @param productIdList 商品 ID 列表
	 * @return 用户收藏的指定商品 ID 列表
	 */
	@PostMapping(PREFIX + "/favoriteProduct/getFavouriteProductId")
	R<List<FavoriteProductDTO>> getFavouriteProductId(@RequestParam(value = "userId") Long userId, @RequestBody List<Long> productIdList);

}
