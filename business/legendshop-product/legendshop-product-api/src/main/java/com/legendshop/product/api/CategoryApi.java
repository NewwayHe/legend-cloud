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
import com.legendshop.product.bo.CategoryBO;
import com.legendshop.product.dto.CategoryTree;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "categoryApi", value = ServiceNameConstants.PRODUCT_SERVICE)
public interface CategoryApi {

	String PREFIX = ServiceNameConstants.PRODUCT_SERVICE_RPC_PREFIX;

	@PostMapping(PREFIX + "/category/getCategoryByIds")
	R<List<CategoryBO>> getCategoryByIds(@RequestBody List<Long> categoryIds);

	@GetMapping(PREFIX + "/category/getCategoryNameById")
	R<String> getCategoryNameById(@RequestParam("globalFirstCatId") Long globalFirstCatId);

	@GetMapping(PREFIX + "/category/getDecorateCategoryList")
	R<String> getDecorateCategoryList();

	@GetMapping(PREFIX + "/category/getTreeById")
	R<List<CategoryTree>> getTreeById(@RequestParam("productCategoryRoot") Long productCategoryRoot);

	@GetMapping(PREFIX + "/category/getById")
	R<CategoryBO> getById(@RequestParam("categoryId") Long categoryId);
}
