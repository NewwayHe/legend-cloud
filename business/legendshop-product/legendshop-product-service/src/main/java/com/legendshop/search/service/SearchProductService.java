/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.service;

import com.legendshop.search.dto.ProductDocumentDTO;
import com.legendshop.search.vo.SearchRequest;
import com.legendshop.search.vo.SearchResult;

import java.util.List;

/**
 * 搜索商品
 *
 * @author legendshop
 */
public interface SearchProductService {


	SearchResult<ProductDocumentDTO> searchProduct(SearchRequest request, Long userId);

	ProductDocumentDTO searchProductById(Long productId);

	List<ProductDocumentDTO> searchProductListByIds(List<Long> productIdList);

	List<ProductDocumentDTO> searchProductListByIds(List<Long> productIdList, String orderBy, int count);

	List<ProductDocumentDTO> searchProductByCategoryId(Long categoryId, int pageSize);

	SearchResult<ProductDocumentDTO> searchProductListByIds(List<Long> productIdList, SearchRequest request);

	List<ProductDocumentDTO> getProductByGroup(Long prodGroupId, int count);

	SearchResult<ProductDocumentDTO> getProductPageListByGroup(Long prodGroupId, Integer curPageNO);

	List<ProductDocumentDTO> searchShopProductByCategoryId(Long categoryId, Integer pageSize);

	/**
	 * 根据标签ID查询商品
	 *
	 * @param tagId
	 * @return
	 */
	List<ProductDocumentDTO> queryByTagId(Long tagId);
}
