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
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.DraftSkuDTO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.dto.SkuDTO;
import com.legendshop.product.entity.DraftSku;
import com.legendshop.product.entity.Product;

import java.util.Date;
import java.util.List;

/**
 * 单品SKU草稿表(DraftSku)表服务接口
 *
 * @author legendshop
 * @since 2022-05-08 09:37:06
 */
public interface DraftSkuService {

	/**
	 * 保存sku到草稿表
	 *
	 * @param skuList
	 * @return
	 */
	R<Void> save(List<SkuDTO> skuList);

	/**
	 * 更新sku到草稿表
	 *
	 * @param productDTO
	 * @param originProduct
	 * @param date
	 * @return
	 */
	List<DraftSkuDTO> updateSku(ProductDTO productDTO, Product originProduct, Date date);

	/**
	 * 根据商品Id删除sku草稿
	 *
	 * @param productId
	 */
	Integer deleteByProductId(Long productId);

	/**
	 * 根据商品ID获取skuBO
	 *
	 * @param productId
	 * @return
	 */
	List<SkuBO> getByProductId(Long productId);

	/**
	 * 根据商品ID获取
	 *
	 * @param productId
	 * @return
	 */
	List<DraftSku> getSkuByProductId(Long productId);


}
