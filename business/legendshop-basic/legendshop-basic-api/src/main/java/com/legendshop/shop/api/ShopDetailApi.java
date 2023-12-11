/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.api;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.shop.bo.ShopDetailBO;
import com.legendshop.shop.dto.SearchShopDTO;
import com.legendshop.shop.dto.ShopDetailDTO;
import com.legendshop.shop.query.SearchShopQuery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 店铺详情服务
 *
 * @author legendshop
 */
@FeignClient(contextId = "shopDetailApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface ShopDetailApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	/**
	 * 根据ID获取店铺详情信息
	 *
	 * @param id
	 * @return
	 */
	@PostMapping(PREFIX + "/shopDetail/getById")
	R<ShopDetailDTO> getById(@RequestParam(value = "id") Long id);


	/**
	 * 根据ID获取店铺详情信息
	 *
	 * @param shopIds
	 * @return
	 */
	@PostMapping(PREFIX + "/shopDetail/queryByIds")
	R<List<ShopDetailDTO>> queryByIds(@RequestBody List<Long> shopIds);


	/**
	 * 根据商家用户ID获取店铺详情信息
	 *
	 * @param userId
	 * @return
	 */
	@PostMapping(PREFIX + "/shopDetail/getByUserId")
	R<ShopDetailDTO> getByUserId(@RequestParam(value = "userId") Long userId);

	/**
	 * 商品详情店铺信息
	 *
	 * @param shopId
	 * @return
	 */
	@GetMapping(PREFIX + "/shopDetail/getUserShop")
	R<ShopDetailBO> getUserShop(@RequestParam(value = "userId", required = false) Long userId, @RequestParam(value = "shopId") Long shopId);

	/**
	 * 更新购买数
	 *
	 * @param shopId
	 * @return
	 */
	@PostMapping(PREFIX + "/shopDetail/updateBuys")
	R<Boolean> updateBuys(@RequestParam(value = "shopId") Long shopId, @RequestParam(value = "buys") Integer buys);

	@GetMapping(PREFIX + "/shopDetail/queryAll")
	R<List<ShopDetailDTO>> queryAll();

	/**
	 * 查找所有店铺
	 *
	 * @param query
	 * @return
	 */
	@PostMapping(PREFIX + "/shopDetail/searchShopEs")
	R<PageSupport<SearchShopDTO>> searchShopEs(@RequestBody SearchShopQuery query);

	/**
	 * 查找所有店铺
	 *
	 * @param query
	 * @return
	 */
	@PostMapping(PREFIX + "/shopDetail/searchAllShop")
	Long searchAllShop(@RequestBody SearchShopQuery query);

	/**
	 * ES 根据店铺ID查找店铺信息
	 *
	 * @param userId
	 * @param shopId
	 * @return
	 */
	@GetMapping(PREFIX + "/shopDetail/getUserShopES")
	R<ShopDetailBO> getUserShopEs(@RequestParam(value = "userId", required = false) Long userId, @RequestParam(value = "shopId") Long shopId);
}
