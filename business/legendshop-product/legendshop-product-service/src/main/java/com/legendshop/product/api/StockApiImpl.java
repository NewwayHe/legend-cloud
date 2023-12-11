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
import com.legendshop.product.dto.BatchUpdateStockDTO;
import com.legendshop.product.dto.InventoryOperationsDTO;
import com.legendshop.product.service.StockService;
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
public class StockApiImpl implements StockApi {

	final StockService stockService;

	@Override
	public R makeUpSalesStocks(Long skuId, Integer basketCount) {
		stockService.makeUpSalesStocks(skuId, basketCount);
		return R.ok();
	}

	@Override
	public R makeUpSalesStocksByOrderType(Long skuId, Integer basketCount, String orderType) {
		stockService.makeUpSalesStocksByOrderType(skuId, basketCount, orderType);
		return R.ok();
	}

	@Override
	public R makeUpStocks(Long skuId, Integer basketCount) {
		stockService.makeUpStocks(skuId, basketCount);
		return R.ok();
	}

	@Override
	public R<Boolean> reduceHold(Long productId, Long skuId, Integer totalCount) {
		return R.ok(stockService.reduceHold(productId, skuId, totalCount));
	}

	@Override
	public R<Boolean> addActualHold(Long productId, Long skuId, Integer basketCount) {
		return R.ok(stockService.addActualHold(productId, skuId, basketCount));
	}

	@Override
	public R batchDeductionInventory(List<BatchUpdateStockDTO> batchUpdateStockDTO) {
		stockService.batchDeductionInventory(batchUpdateStockDTO);
		return R.ok();
	}

	@Override
	public R<Void> inventoryOperations(List<InventoryOperationsDTO> operationsList) {
		return this.stockService.inventoryOperations(operationsList);
	}

	@Override
	public R deductionInventory(Long productId, Long skuId, Integer basketCount) {
		try {
			stockService.deductionInventory(productId, skuId, basketCount);
		} catch (Exception e) {
			return R.fail(e.getMessage());
		}
		return R.ok();
	}
}
