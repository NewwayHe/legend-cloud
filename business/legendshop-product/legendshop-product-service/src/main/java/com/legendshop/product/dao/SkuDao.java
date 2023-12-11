/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao;


import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.InventoryOperationsDTO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.dto.SkuAmountDTO;
import com.legendshop.product.dto.SkuUpdateSkuDTO;
import com.legendshop.product.entity.Sku;
import com.legendshop.product.excel.StockExportDTO;
import com.legendshop.product.query.ProductQuery;
import com.legendshop.product.query.SkuQuery;

import java.util.List;

/**
 * 单品Dao.
 *
 * @author legendshop
 */
public interface SkuDao extends GenericDao<Sku, Long> {

	List<Sku> getSkuByProductId(Long productId);

	Integer getStocksByLockMode(Long skuId);

	Integer getActualStocksByLockMode(Long skuId);

	/**
	 * 更新sku销售库存
	 *
	 * @param skuId
	 * @param stocks(±库存数，可增可减)
	 */
	Integer updateSkuStocks(Long skuId, long stocks);

	/**
	 * 减少sku销售库存
	 *
	 * @param skuId  skuID
	 * @param stocks 减少数量
	 * @return
	 */
	Integer directDeductionSkuStocks(Long skuId, long stocks);

	/**
	 * 增加sku销售库存
	 *
	 * @param skuId  skuID
	 * @param stocks 增加数量
	 * @return
	 */
	Integer directAddSkuStocks(Long skuId, long stocks);


	/**
	 * 增加sku销售库存
	 *
	 * @param skuId  skuID
	 * @param stocks 增加数量
	 * @return
	 */
	Integer directDeductionAddSkuStocks(Long skuId, long stocks);

	/**
	 * 更新sku实际库存
	 *
	 * @param skuId
	 * @param basketCount (±库存数，可增可减)
	 */
	int updateSkuActualStocks(Long skuId, long basketCount);

	Sku getByProductIdSkuId(Long productId, Long skuId);

	List<Sku> getSku(Long[] skuId);

	/**
	 * 根据skuId 修改 skuType
	 *
	 * @param skuId
	 * @param skuType       修改为目标活动类型
	 * @param originSkuType 原来的活动类型
	 */
	int updateSkuTypeById(Long skuId, String skuType, String originSkuType);

	int updateStocksBySkuId(Long skuId, Integer basketCount);

	/**
	 * 根据skuId修改sku的类型
	 *
	 * @param skuIds
	 * @param skuType
	 * @param originalSkuType 原始sku状态，活动结束还原sku时需传
	 */
	void batchUpdateSkuType(List<Long> skuIds, String skuType, String originalSkuType);

	/**
	 * 根据SkuId补充库存 [ 实际库存、销售库存 ]
	 */
	void updateSkuAllStocks(Long skuId, Integer basketCount);

	/**
	 * 库存预警分页查询
	 *
	 * @param query
	 * @return
	 */
	PageSupport<SkuBO> stocksPage(ProductQuery query);

	/**
	 * 库存预警查询
	 *
	 * @param query
	 * @return
	 */
	List<StockExportDTO> stocksExport(ProductQuery query);

	/**
	 * 库存列表
	 *
	 * @param query
	 * @return
	 */
	List<SkuBO> stocksList(SkuQuery query);

	/**
	 * 根据商品idList查询sku列表
	 *
	 * @param productId
	 * @return
	 */
	List<Sku> queryByProductId(List<Long> productId);

	/**
	 * 根据skuIdList查询库存列表
	 *
	 * @param skuIdList
	 * @return
	 */
	List<SkuBO> queryStocksListBySkuIdList(List<Long> skuIdList);

	/**
	 * 通过id集合获取sku列表
	 *
	 * @param skuIds
	 * @return
	 */
	List<Sku> queryBySkuIds(List<Long> skuIds);

	/**
	 * 根据productList查询sku集合
	 *
	 * @param productIdList
	 * @return
	 */
	List<Sku> querySkuByProductIdList(List<Long> productIdList);

	/**
	 * 物理删除sku
	 *
	 * @param prodIds
	 */
	void deleteByProdId(List<Long> prodIds);

	/**
	 * 根根据商品Id查询对应的Sku售价
	 *
	 * @param prodIds 商品ID列表
	 * @return Sku售价
	 */
	List<SkuAmountDTO> querySkuAmount(List<Long> prodIds);

	/**
	 * 根据商品Id查询对应的Sku售价
	 *
	 * @param prodId 商品ID
	 * @return Sku售价
	 */
	List<SkuAmountDTO> querySkuAmount(Long prodId);

	/**
	 * 查询商家优惠券的SKU列表
	 *
	 * @param shopIds 商家ID列表
	 * @return 商家优惠券的SKU列表
	 */
	List<SkuBO> queryCouponSkuByShopId(List<Long> shopIds);

	/**
	 * 减少SKU的库存
	 *
	 * @param skuId SKU ID
	 * @param stock 减少的库存量
	 * @return 减少库存操作是否成功
	 */
	boolean reduceStock(Long skuId, Integer stock);

	/**
	 * 增加SKU的库存
	 *
	 * @param skuId SKU ID
	 * @param stock 增加的库存量
	 * @return 增加库存操作是否成功
	 */
	boolean addBackStock(Long skuId, Integer stock);

	/**
	 * 根据SKU ID列表查询SKU的业务对象列表
	 *
	 * @param skuIds SKU ID列表
	 * @return SKU的业务对象列表
	 */
	List<SkuBO> queryBOBySkuIds(List<Long> skuIds);

	/**
	 * 计算商品购买数量
	 *
	 * @param numberList 商品编号列表
	 * @return SKU的业务对象列表
	 */
	List<SkuBO> calculateBuys(List<String> numberList);

	/**
	 * 更新商品购买数量
	 *
	 * @param id   商品ID
	 * @param buys 购买数量
	 * @return 更新购买数量操作是否成功
	 */
	boolean updateBuys(Long id, Integer buys);


	/**
	 * 【商家】首页库存预警分页查询
	 *
	 * @param productQuery
	 * @return
	 */
	PageSupport<SkuBO> stocksArmPage(ProductQuery productQuery);

	void cleanCache(SkuUpdateSkuDTO skuDTO);

	/**
	 * 根据productId集合获取skuBO
	 *
	 * @param productIdList
	 * @return
	 */
	List<SkuBO> querySkuBOByProductIdList(List<Long> productIdList);

	/**
	 * 根据productId获取skuBO
	 *
	 * @param productId
	 * @return
	 */
	List<SkuBO> getSkuBOByProductId(Long productId);

	/**
	 * 根据skuId查找销售库存
	 *
	 * @param skuId
	 * @return
	 */
	Integer getSkuStock(Long skuId);

	/**
	 * 更具商品ID查找销售库存
	 *
	 * @param productId
	 * @return
	 */
	Integer getProductStock(Long productId);

	/**
	 * 获取sku数量
	 *
	 * @param productIdList
	 * @return
	 */
	List<ProductDTO> querySkuCount(List<Long> productIdList);

	/**
	 * 批量修改实际库存
	 */
	int[] updateAllSkuStocks(List<InventoryOperationsDTO> dtoList);

	/**
	 * 批量修改销售库存
	 */
	int[] updateSalesSkuStocks(List<InventoryOperationsDTO> dtoList);

	/**
	 * 批量修改实际库存
	 */
	int[] updateActualSkuStocks(List<InventoryOperationsDTO> dtoList);

	/**
	 * 根据skuId查询商品给客服使用
	 *
	 * @param skuId
	 * @return
	 */
	SkuBO getSkuByIdToCustom(Long skuId);
}
