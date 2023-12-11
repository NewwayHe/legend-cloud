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
import com.legendshop.product.bo.CategoryBO;
import com.legendshop.product.dto.CategoryTree;
import com.legendshop.product.service.CategoryService;
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
public class CategoryApiImpl implements CategoryApi {

	final CategoryService categoryService;

	@Override
	public R<List<CategoryBO>> getCategoryByIds(List<Long> categoryIds) {
		return R.ok(categoryService.getCategoryByIds(categoryIds));
	}

	@Override
	public R<String> getCategoryNameById(Long globalFirstCatId) {
		return R.ok(categoryService.getCategoryNameById(globalFirstCatId));
	}

	@Override
	public R<String> getDecorateCategoryList() {
		return categoryService.getDecorateCategoryList();
	}

	@Override
	public R<List<CategoryTree>> getTreeById(Long productCategoryRoot) {
		return categoryService.getTreeById(productCategoryRoot);
	}

	@Override
	public R<CategoryBO> getById(Long categoryId) {
		return R.ok(categoryService.getById(categoryId));
	}
}
