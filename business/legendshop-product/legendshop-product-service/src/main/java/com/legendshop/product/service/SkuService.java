/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import com.legendshop.common.core.dto.KeyValueEntityDTO;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.ActivityProductDTO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.dto.ProductPropertyDTO;
import com.legendshop.product.dto.SkuDTO;

import java.util.List;

/**
 * 单品SKU服务.
 *
 * @author legendshop
 */
public interface SkuService {

	int updateSku(SkuBO skuDTO);

	List<SkuBO> getSkuByProduct(Long productId);

	SkuBO getByProductIdSkuId(Long productId, Long skuId);

	List<KeyValueEntityDTO> getSkuImg(String skuProp);

	/**
	 * 获取属性和属性值
	 *
	 * @param activityProductDTO
	 * @return
	 */
	List<ProductPropertyDTO> getPropValListByProd(ActivityProductDTO activityProductDTO);

	/**
	 * 根据 skuId 查找sku
	 *
	 * @param skuId
	 */
	SkuBO getSkuById(Long skuId);

	/**
	 * 根据skuId 修改活动类型
	 *
	 * @param skuId
	 * @param skuType       修改为目标活动类型
	 * @param originSkuType 原来的活动类型
	 */
	int updateSkuTypeById(Long skuId, String skuType, String originSkuType);

	/**
	 * 根据skuId修改sku的类型
	 *
	 * @param skuIds
	 * @param skuType
	 * @param originalSkuType 原始sku状态，活动结束还原sku时需传
	 */
	void batchUpdateSkuType(List<Long> skuIds, String skuType, String originalSkuType);

	/**
	 * 根据productId 修改 skuType
	 *
	 * @param productId
	 * @param skuType
	 * @param originSkuType
	 */
	int updateSkuTypeByProductId(Long productId, String skuType, String originSkuType);

	int updateStocksBySkuId(Long skuId, Integer basketCount);

	List<SkuDTO> queryBySkuIds(List<Long> skuIds);

	/**
	 * 根据productList查询sku集合
	 *
	 * @param productIdList
	 * @return
	 */
	List<SkuBO> querySkuByProductIdList(List<Long> productIdList);

	/**
	 * 清空sku缓存
	 *
	 * @param skuIds
	 */
	void clearSkuCache(List<Long> skuIds);


	List<SkuBO> queryCouponSkuByShopId(List<Long> shopIds);

	/**
	 * 减库存
	 *
	 * @param skuId
	 * @param stock
	 * @return
	 */
	boolean reduceStock(Long skuId, Integer stock);

	/**
	 * 加回库存,活动结束
	 *
	 * @param skuId
	 * @param stock
	 * @return
	 */
	boolean addBackStock(Long skuId, Integer stock);


	List<SkuBO> queryBOBySkuIds(List<Long> skuIds);

	/**
	 * 根据SKU ID查找库存
	 *
	 * @param skuId
	 * @return
	 */
	Integer getSkuStock(Long skuId);

	/**
	 * 更具商品ID查找商品库存
	 *
	 * @param productId 商品ID
	 * @return
	 */
	Integer getProductStock(Long productId);

	/**
	 * 根据productList查询skuBO集合
	 *
	 * @param productIdList
	 * @return
	 */
	List<SkuBO> querySkuBOByProductIdList(List<Long> productIdList);

	/**
	 * 根据productId获取skuBO集合
	 *
	 * @param productId
	 * @return
	 */
	List<SkuBO> getSkuBOByProduct(Long productId);

	/**
	 * 获取sku数量
	 *
	 * @param productIdList
	 * @return
	 */
	List<ProductDTO> querySkuCount(List<Long> productIdList);

	/**
	 * 根据skuId查询商品给客服使用
	 *
	 * @param skuId
	 * @return
	 */
	SkuBO getSkuByIdToCustom(Long skuId);
}
