/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.search.dto.CouponDocumentDTO;
import com.legendshop.search.dto.ShopDocumentDTO;
import com.legendshop.search.service.SearchCouponIndexService;
import com.legendshop.search.service.SearchCouponService;
import com.legendshop.search.service.SearchShopService;
import com.legendshop.search.service.strategy.IndexServiceContext;
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
public class EsIndexApiImpl implements EsIndexApi {

	private final IndexServiceContext indexServiceContext;
	private final SearchShopService searchShopService;
	private final SearchCouponService searchCouponService;
	private final SearchCouponIndexService searchCouponIndexService;


	@Override
	public List<CouponDocumentDTO> queryCouponBySkuIdsAndShopId(List<Long> skuIds, Long shopId) {
		return searchCouponService.queryCouponBySkuIdsAndShopId(skuIds, shopId);
	}

	@Override
	public ShopDocumentDTO getShopById(Long shopId) {
		return searchShopService.getShopById(shopId);
	}

	@Override
	public R<Boolean> reIndex(String indexType, Integer targetMethod, Integer targetType, String targetId) {
		Boolean result = indexServiceContext.createReIndexLog(indexType, targetMethod, targetType, targetId);
		return R.ok(result);
	}

	@Override
	public R<Void> createCouponIndexByCouponId(Long couponId) {
		return searchCouponIndexService.initByCouponIdToCouponIndex(couponId);
	}

	@Override
	public R<Void> deleteCouponIndexByCouponId(Long couponId) {
		return searchCouponIndexService.delByCouponIdToCouponIndex(couponId);
	}
}
