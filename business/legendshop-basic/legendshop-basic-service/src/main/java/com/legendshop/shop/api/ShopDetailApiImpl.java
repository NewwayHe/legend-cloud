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
import com.legendshop.shop.bo.ShopDetailBO;
import com.legendshop.shop.dto.SearchShopDTO;
import com.legendshop.shop.dto.ShopDetailDTO;
import com.legendshop.shop.query.SearchShopQuery;
import com.legendshop.shop.service.ShopDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商城详细信息
 *
 * @author legendshop
 */
@RestController
@RequiredArgsConstructor
public class ShopDetailApiImpl implements ShopDetailApi {

	private final ShopDetailService shopDetailService;


	@Override
	public R<ShopDetailDTO> getById(@RequestParam Long id) {
		return R.ok(shopDetailService.getById(id));
	}

	@Override
	public R<List<ShopDetailDTO>> queryByIds(List<Long> shopIds) {
		return R.ok(shopDetailService.queryByIds(shopIds));
	}

	@Override
	public R<ShopDetailDTO> getByUserId(Long userId) {
		return R.ok(shopDetailService.getByUserId(userId));
	}

	/**
	 * 获取商家店铺统计信息 评分
	 *
	 * @param shopId
	 * @return
	 */
	@Override
	public R<ShopDetailBO> getUserShop(@RequestParam(required = false) Long userId, @RequestParam Long shopId) {
		return shopDetailService.getUserShop(userId, shopId);
	}

	@Override
	public R<Boolean> updateBuys(Long shopId, Integer buys) {
		return shopDetailService.updateBuys(shopId, buys);
	}

	@Override
	public R<List<ShopDetailDTO>> queryAll() {
		return shopDetailService.queryAll();
	}

	@Override
	public R<PageSupport<SearchShopDTO>> searchShopEs(@RequestBody SearchShopQuery query) {
		return R.ok(shopDetailService.searchShop(query));
	}

	@Override
	public Long searchAllShop(@RequestBody SearchShopQuery query) {
		return shopDetailService.searchAllShop(query);
	}

	@Override
	public R<ShopDetailBO> getUserShopEs(@RequestParam(value = "userId", required = false) Long userId, @RequestParam(value = "shopId") Long shopId) {
		return R.ok(shopDetailService.getUserShopEs(userId, shopId));
	}


}
