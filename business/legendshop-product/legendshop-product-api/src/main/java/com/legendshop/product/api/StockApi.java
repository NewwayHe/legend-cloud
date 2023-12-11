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
import com.legendshop.product.dto.BatchUpdateStockDTO;
import com.legendshop.product.dto.InventoryOperationsDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "stockApi", value = ServiceNameConstants.PRODUCT_SERVICE)
public interface StockApi {

	String PREFIX = ServiceNameConstants.PRODUCT_SERVICE_RPC_PREFIX;

	/**
	 * 取消订单补库存
	 *
	 * @param skuId
	 * @param basketCount
	 * @return
	 */
	@GetMapping(PREFIX + "/stock/makeUpSalesStocks")
	R makeUpSalesStocks(@RequestParam("skuId") Long skuId, @RequestParam("basketCount") Integer basketCount);


	/**
	 * 活动结束失效回归销售库存
	 *
	 * @param skuId
	 * @param basketCount
	 * @param orderType
	 * @return
	 */
	@GetMapping(PREFIX + "/stock/makeUpSalesStocksByOrderType")
	R makeUpSalesStocksByOrderType(@RequestParam("skuId") Long skuId, @RequestParam("basketCount") Integer basketCount, @RequestParam("orderType") String orderType);

	/**
	 * 退款退货补库存
	 */
	@GetMapping(PREFIX + "/stock/makeUpStocks")
	R makeUpStocks(@RequestParam("skuId") Long skuId, @RequestParam("basketCount") Integer basketCount);


	@GetMapping(PREFIX + "/stock/addHold")
	R<Boolean> reduceHold(@RequestParam("productId") Long productId, @RequestParam("skuId") Long skuId,
						  @RequestParam("totalCount") Integer totalCount);


	/**
	 * 发货减少实际库存
	 *
	 * @param productId   商品Id
	 * @param skuId       单品Id
	 * @param basketCount 订购数
	 * @return 成功与否
	 */
	@GetMapping(PREFIX + "/stock/addActualHold")
	R<Boolean> addActualHold(@RequestParam("productId") Long productId, @RequestParam("skuId") Long skuId,
							 @RequestParam("basketCount") Integer basketCount);

	/**
	 * 扣减sku商品销售库存（sku和spu都会更新）
	 * 下单时候扣减（普通订单活动订单）
	 *
	 * @param batchUpdateStockDTO
	 * @return
	 */
	@PostMapping(PREFIX + "/stock/batchDeductionInventory")
	R batchDeductionInventory(@Valid @RequestBody List<BatchUpdateStockDTO> batchUpdateStockDTO);

	@PostMapping(value = PREFIX + "/stock/inventoryOperations")
	R<Void> inventoryOperations(@RequestBody List<InventoryOperationsDTO> operationsList);

	/**
	 * 扣减sku商品销售库存（sku和spu都会更新）
	 *
	 * @param productId   商品id
	 * @param skuId       skuId
	 * @param basketCount 扣减数量
	 * @return
	 */
	@GetMapping(PREFIX + "/stock/deductionInventory")
	R deductionInventory(@RequestParam("productId") Long productId, @RequestParam("skuId") Long skuId, @RequestParam("basketCount") Integer basketCount);
}
