/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import com.legendshop.basic.dto.AuditDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.product.bo.ProductBO;
import com.legendshop.product.bo.ProductUpdateBO;
import com.legendshop.product.dto.DraftProductDTO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.entity.DraftProduct;
import com.legendshop.product.entity.Product;
import com.legendshop.shop.dto.ShopDetailDTO;

import java.util.List;

/**
 * 商品SPU草稿表(DraftProduct)表服务接口
 *
 * @author legendshop
 * @since 2022-05-08 09:37:11
 */
public interface DraftProductService {

	/**
	 * 根据商品ID获取草稿转换成的商品信息(用于用户端页面展示)
	 *
	 * @param productId
	 * @return
	 */
	ProductBO getProductBOByProductId(Long productId);

	/**
	 * 根据商品ID获取草稿转换成的商品信息（用于商家端编辑展示）
	 *
	 * @param productId
	 * @return
	 */
	ProductUpdateBO getProductUpdateBOByProductId(Long productId, Long shopId);

	/**
	 * 根据商品Id获取草稿
	 *
	 * @param productId
	 * @return
	 */
	DraftProductDTO getByProductId(Long productId);

	/**
	 * 保存商品草稿
	 *
	 * @param productDTO
	 * @return
	 */
	R<Void> save(ProductDTO productDTO);

	/**
	 * 更新商品草稿
	 *
	 * @param productDTO
	 * @return
	 */
	R<Void> update(ProductDTO productDTO);

	/**
	 * 更新商品草稿
	 *
	 * @param productDTO      更新商品内容
	 * @param originalProduct 原商品内容
	 * @param shopDetail      店铺信息
	 * @return
	 */
	R<Void> update(ProductDTO productDTO, Product originalProduct, ShopDetailDTO shopDetail);

	/**
	 * 发布草稿商品
	 *
	 * @param productId
	 * @param auditOpinion
	 * @return
	 */
	R<Void> release(Long productId, String auditOpinion);

	/**
	 * 审核商品
	 *
	 * @param auditDTO
	 * @return
	 */
	R audit(AuditDTO auditDTO);

	/**
	 * 草稿提审
	 *
	 * @param productId
	 * @return
	 */
	R<Void> arraignment(Long productId);

	/**
	 * 根据商品ID删除草稿
	 *
	 * @param productIds
	 * @return
	 */
	Integer deleteByProductId(List<Long> productIds);

	/**
	 * 根据商品ID删除草稿
	 *
	 * @param productId
	 * @return
	 */
	Integer deleteByProductId(Long productId);

	/**
	 * 草稿提审撤销
	 *
	 * @param productId
	 * @return
	 */
	R revokeArraignment(Long productId);

	/**
	 * 修改商品
	 *
	 * @param draftProduct
	 * @param auditOpinion
	 * @return
	 */
	R updateProduct(DraftProduct draftProduct, String auditOpinion);
}
