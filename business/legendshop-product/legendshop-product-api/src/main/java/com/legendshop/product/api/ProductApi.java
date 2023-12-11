/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.api;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.product.bo.DecorateProductBO;
import com.legendshop.product.bo.HotSellProductBO;
import com.legendshop.product.bo.ProductBO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.query.ProductQuery;
import com.legendshop.shop.dto.ShopDecorateProductQuery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "productApi", value = ServiceNameConstants.PRODUCT_SERVICE)
public interface ProductApi {

	String PREFIX = ServiceNameConstants.PRODUCT_SERVICE_RPC_PREFIX;

	@PostMapping(PREFIX + "/queryProductOnLine")
	R<PageSupport<ProductDTO>> queryProductOnLine(@RequestBody ProductQuery query);

	@PostMapping(PREFIX + "/queryProductOnLineEs")
	R<PageSupport<ProductDTO>> queryProductOnLineEs(@RequestBody ProductQuery query);

	@PostMapping(PREFIX + "/queryProductOnLineEsByProductId")
	R<List<ProductDTO>> queryProductOnLineEsByProductId(@RequestBody List<Long> productIds);

	@PostMapping(PREFIX + "/getProductOnLineEsByProductId")
	R<ProductDTO> getProductOnLineEsByProductId(@RequestParam("productId") Long productId);

	@PostMapping(PREFIX + "/getPage")
	R<PageSupport<ProductBO>> getPage(@RequestBody ProductQuery productQuery);

	@PostMapping(PREFIX + "/queryDecorateProductList")
	R<PageSupport<DecorateProductBO>> queryDecorateProductList(@RequestBody ProductQuery productQuery);

	@PostMapping(PREFIX + "/queryShopDecorateProductList")
	R<PageSupport<DecorateProductBO>> queryDecorateProductList(@RequestBody ShopDecorateProductQuery shopDecorateProductQuery);

	@PostMapping(PREFIX + "/queryAllByIds")
	R<List<ProductDTO>> queryAllByIds(@RequestBody List<Long> productId);

	@GetMapping(PREFIX + "/getProductListByGroupId")
	R<List<ProductDTO>> getProductListByGroupId(@RequestParam("productGroupId") Long productGroupId, @RequestParam("pageSize") Integer pageSize);

	@GetMapping(PREFIX + "/queryHotSellProdByShopId")
	R<List<HotSellProductBO>> queryHotSellProdByShopId(@RequestParam("shopId") Long shopId);

	@GetMapping(PREFIX + "/getDtoByProductId")
	R<ProductDTO> getDtoByProductId(@RequestParam("id") Long id);

	@GetMapping(PREFIX + "/getOnlineDtoByProductId")
	R<ProductDTO> getOnlineDtoByProductId(@RequestParam("productId") Long productId);

	@GetMapping(PREFIX + "/getBoByProductId")
	R<ProductBO> getBoByProductId(@RequestParam("productId") Long productId);

	/**
	 * 下线商家所有的商品
	 *
	 * @param ids shopIds
	 * @return
	 */
	@PostMapping(PREFIX + "/offLineByShopIds")
	R<Void> offLineByShopIds(@RequestBody List<Long> ids);

	/**
	 * 根据店铺ID和操作状态枚举获取商品数量的方法。
	 *
	 * @param shopId       店铺ID
	 * @param opStatusEnum 操作状态枚举
	 * @return 商品数量
	 */
	@GetMapping(PREFIX + "/getProductCountByShopId")
	R<Long> getProductCountByShopId(@RequestParam("shopId") Long shopId, @RequestParam("opStatusEnum") OpStatusEnum opStatusEnum);

	/**
	 * 根据商品ID更新库存的方法。
	 *
	 * @param id          商品ID
	 * @param basketCount 购物篮数量
	 * @return 更新的库存数量
	 */
	@PutMapping(PREFIX + "/updateStocksByProductId")
	R<Integer> updateStocksByProductId(@RequestParam("id") Long id, @RequestParam("basketCount") Integer basketCount);

	/**
	 * 获取商家所有商品的方法。
	 *
	 * @return 商品索引总数
	 */
	@PostMapping(PREFIX + "/LineByShopIds")
	Long getProductIndexCount();
}
