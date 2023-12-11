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
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.search.dto.ProductDocumentDTO;
import com.legendshop.search.vo.SearchResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "searchProductApi", value = ServiceNameConstants.PRODUCT_SERVICE)
public interface SearchProductApi {

	String PREFIX = ServiceNameConstants.PRODUCT_SERVICE_RPC_PREFIX;

	@GetMapping(PREFIX + "/product/searchProductByCategoryId")
	R<List<ProductDocumentDTO>> searchProductByCategoryId(@RequestParam("categoryId") Long categoryId, @RequestParam("pageSize") Integer pageSize);

	@GetMapping(PREFIX + "/product/getProductPageListByGroup")
	R<SearchResult<ProductDocumentDTO>> getProductPageListByGroup(@RequestParam("prodGroupId") Long prodGroupId, @RequestParam("curPageNO") Integer curPageNO);

	@GetMapping(PREFIX + "/product/searchShopProductByCategoryId")
	R<List<ProductDocumentDTO>> searchShopProductByCategoryId(@RequestParam("categoryId") Long categoryId, @RequestParam("pageSize") Integer pageSize);

	@GetMapping(PREFIX + "/product/searchProductById")
	R<ProductDocumentDTO> searchProductById(@RequestParam("productId") Long productId);

	@PostMapping(PREFIX + "/product/searchProductByIds")
	R<List<ProductDocumentDTO>> searchProductByIds(@RequestBody List<Long> productIds);

}
