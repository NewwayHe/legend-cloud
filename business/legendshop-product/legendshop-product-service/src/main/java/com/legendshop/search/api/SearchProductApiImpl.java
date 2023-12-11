/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.search.dto.ProductDocumentDTO;
import com.legendshop.search.service.SearchProductService;
import com.legendshop.search.vo.SearchResult;
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
public class SearchProductApiImpl implements SearchProductApi {

	private final SearchProductService searchProductService;

	@Override
	public R<List<ProductDocumentDTO>> searchProductByCategoryId(Long categoryId, Integer pageSize) {
		return R.ok(searchProductService.searchProductByCategoryId(categoryId, pageSize));
	}

	@Override
	public R<SearchResult<ProductDocumentDTO>> getProductPageListByGroup(Long prodGroupId, Integer curPageNO) {
		return R.ok(searchProductService.getProductPageListByGroup(prodGroupId, curPageNO));
	}

	@Override
	public R<List<ProductDocumentDTO>> searchShopProductByCategoryId(Long categoryId, Integer pageSize) {
		return R.ok(searchProductService.searchShopProductByCategoryId(categoryId, pageSize));
	}

	@Override
	public R<ProductDocumentDTO> searchProductById(Long productId) {
		ProductDocumentDTO productDocumentDTO = searchProductService.searchProductById(productId);
		return R.ok(productDocumentDTO);
	}

	@Override
	public R<List<ProductDocumentDTO>> searchProductByIds(List<Long> productIds) {
		return R.ok(searchProductService.searchProductListByIds(productIds));
	}
}
