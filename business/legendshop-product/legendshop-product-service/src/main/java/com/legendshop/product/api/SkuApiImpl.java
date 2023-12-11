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
import com.legendshop.common.core.dto.KeyValueEntityDTO;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.ActivityProductDTO;
import com.legendshop.product.dto.ProductPropertyDTO;
import com.legendshop.product.dto.SkuDTO;
import com.legendshop.product.service.SkuService;
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
public class SkuApiImpl implements SkuApi {

	final SkuService skuService;

	@Override
	public R<List<SkuBO>> getSkuByProduct(Long productId) {
		return R.ok(skuService.getSkuByProduct(productId));
	}

	@Override
	public R<Integer> updateSkuTypeById(Long skuId, String value, String value1) {
		return R.ok(skuService.updateSkuTypeById(skuId, value, value1));
	}

	@Override
	public R<Void> batchUpdateSkuType(List<Long> skuIds, String skuType, String originalSkuType) {
		skuService.batchUpdateSkuType(skuIds, skuType, originalSkuType);
		return R.ok();
	}

	@Override
	public R<SkuBO> getSkuById(Long skuId) {
		return R.ok(skuService.getSkuById(skuId));
	}

	@Override
	public R<SkuBO> getSkuByIdToCustom(Long skuId) {
		return R.ok(skuService.getSkuByIdToCustom(skuId));
	}

	@Override
	public R updateSku(SkuBO sku) {
		return R.ok(skuService.updateSku(sku));
	}

	@Override
	public R<List<KeyValueEntityDTO>> getSkuImg(String properties) {
		return R.ok(skuService.getSkuImg(properties));
	}

	@Override
	public R<SkuBO> getByProductIdSkuId(Long productId, Long skuId) {
		return R.ok(skuService.getByProductIdSkuId(productId, skuId));
	}

	@Override
	public R<List<SkuBO>> querySkuByProductIdList(List<Long> productIdList) {
		return R.ok(skuService.querySkuByProductIdList(productIdList));
	}

	@Override
	public R<List<SkuDTO>> queryBySkuIds(List<Long> skuIds) {
		return R.ok(skuService.queryBySkuIds(skuIds));
	}

	@Override
	public R updateSkuTypeByProductId(Long productId, String value, String skuType) {
		return R.ok(skuService.updateSkuTypeByProductId(productId, value, skuType));
	}

	@Override
	public R updateStocksBySkuId(Long skuId, Integer basketCount) {
		return R.ok(skuService.updateStocksBySkuId(skuId, basketCount));
	}

	@Override
	public R<List<ProductPropertyDTO>> getPropValListByProd(ActivityProductDTO activityProductDTO) {
		return R.ok(skuService.getPropValListByProd(activityProductDTO));
	}

	@Override
	public R<List<SkuBO>> queryCouponSkuByShopId(List<Long> shopIds) {
		return R.ok(skuService.queryCouponSkuByShopId(shopIds));
	}

	@Override
	public R<Boolean> reduceStock(Long skuId, Integer stock) {
		return R.ok(skuService.reduceStock(skuId, stock));
	}

	@Override
	public R<Boolean> addBackStock(Long skuId, Integer stock) {
		return R.ok(skuService.addBackStock(skuId, stock));
	}

	@Override
	public R<List<SkuBO>> queryBOBySkuIds(List<Long> skuIds) {
		return R.ok(skuService.queryBOBySkuIds(skuIds));
	}
}
