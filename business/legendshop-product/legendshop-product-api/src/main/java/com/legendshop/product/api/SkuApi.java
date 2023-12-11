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
import com.legendshop.common.core.dto.KeyValueEntityDTO;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.ActivityProductDTO;
import com.legendshop.product.dto.ProductPropertyDTO;
import com.legendshop.product.dto.SkuDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "skuApi", value = ServiceNameConstants.PRODUCT_SERVICE)
public interface SkuApi {

	String PREFIX = ServiceNameConstants.PRODUCT_SERVICE_RPC_PREFIX;

	@GetMapping(PREFIX + "/sku/getSkuByProduct")
	R<List<SkuBO>> getSkuByProduct(@RequestParam("productId") Long productId);

	@GetMapping(PREFIX + "/sku/updateSkuTypeById")
	R<Integer> updateSkuTypeById(@RequestParam("skuId") Long skuId, @RequestParam("value") String value, @RequestParam("value1") String value1);

	/**
	 * 根据skuId修改sku的类型
	 *
	 * @param skuIds
	 * @param skuType
	 * @param originalSkuType 原始sku状态，活动结束还原sku时需传
	 */
	@PostMapping(PREFIX + "/sku/batch/update/skuType")
	R<Void> batchUpdateSkuType(@RequestBody List<Long> skuIds, @RequestParam(value = "skuType", required = false) String skuType, @RequestParam(value = "originalSkuType", required = false) String originalSkuType);

	@GetMapping(PREFIX + "/sku/{skuId}")
	R<SkuBO> getSkuById(@PathVariable("skuId") Long skuId);

	@GetMapping(PREFIX + "/sku/getSkuByIdToCustom")
	R<SkuBO> getSkuByIdToCustom(@RequestParam("skuId") Long skuId);

	@PutMapping(PREFIX + "/sku")
	R updateSku(@RequestBody SkuBO sku);

	@GetMapping(PREFIX + "/sku/getSkuImg")
	R<List<KeyValueEntityDTO>> getSkuImg(@RequestParam("properties") String properties);

	@GetMapping(PREFIX + "/sku/getByProductIdSkuId")
	R<SkuBO> getByProductIdSkuId(@RequestParam("productId") Long productId, @RequestParam("skuId") Long skuId);

	@PostMapping(PREFIX + "/sku/querySkuByProductIdList")
	R<List<SkuBO>> querySkuByProductIdList(@RequestBody List<Long> productIdList);

	@PostMapping(PREFIX + "/sku/queryBySkuIds")
	R<List<SkuDTO>> queryBySkuIds(@RequestBody List<Long> skuIds);

	@GetMapping(PREFIX + "/sku/updateSkuTypeByProductId")
	R updateSkuTypeByProductId(@RequestParam("productId") Long productId, @RequestParam("value") String value, @RequestParam("skuType") String skuType);

	@PutMapping(PREFIX + "/sku/updateStocksBySkuId")
	R updateStocksBySkuId(@RequestParam("skuId") Long skuId, @RequestParam("basketCount") Integer basketCount);

	@GetMapping(PREFIX + "/sku/getPropValListByProd")
	R<List<ProductPropertyDTO>> getPropValListByProd(@RequestBody ActivityProductDTO activityProductDTO);

	@PostMapping(PREFIX + "/sku/queryCouponSkuByShopId")
	R<List<SkuBO>> queryCouponSkuByShopId(@RequestBody List<Long> shopIds);

	@PostMapping(PREFIX + "/sku/reduceStock")
	R<Boolean> reduceStock(@RequestParam(value = "skuId") Long skuId, @RequestParam(value = "stock") Integer stock);

	@PostMapping(PREFIX + "/sku/addBackStock")
	R<Boolean> addBackStock(@RequestParam(value = "skuId") Long skuId, @RequestParam("stock") Integer stock);

	@PostMapping(PREFIX + "/sku/queryBOBySkuIds")
	R<List<SkuBO>> queryBOBySkuIds(@RequestBody List<Long> skuIds);
}
