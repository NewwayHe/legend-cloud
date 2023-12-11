/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import com.legendshop.common.core.constant.R;
import com.legendshop.product.dto.BatchUpdateStockDTO;
import com.legendshop.product.dto.InventoryOperationsDTO;

import java.util.List;

/**
 * 库存服务.
 *
 * @author legendshop
 */
public interface StockService {

	/**
	 * 回归销售库存
	 */
	void makeUpSalesStocks(Long skuId, Integer basketCount);

	/**
	 * 退款退货补库存
	 *
	 * @param skuId
	 * @param basketCount 库存数量
	 */
	void makeUpStocks(Long skuId, Integer basketCount);

	/**
	 * 减销售库存
	 *
	 * @param productId
	 * @param skuId
	 * @param basketCount
	 * @return
	 */
	boolean reduceHold(Long productId, Long skuId, Integer basketCount);


	/**
	 * 批量扣销售库存
	 */
	boolean batchDeductionInventory(List<BatchUpdateStockDTO> batchUpdateStockDTO);

	/**
	 * 发货减少实际库存
	 *
	 * @param productId   商品Id
	 * @param skuId       单品Id
	 * @param basketCount 订购数
	 * @return 成功与否
	 */
	boolean addActualHold(Long productId, Long skuId, Integer basketCount);

	/**
	 * 活动结束失效回归销售库存
	 *
	 * @param skuId
	 * @param basketCount
	 * @param orderType
	 */
	void makeUpSalesStocksByOrderType(Long skuId, Integer basketCount, String orderType);

	R<Void> inventoryOperations(List<InventoryOperationsDTO> operationsList);

	/**
	 * 扣库存,允许为负
	 */
	boolean deductionInventory(Long productId, Long skuId, Integer basketCount);
}
