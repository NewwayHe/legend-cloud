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
import com.legendshop.product.bo.ShopCategoryBO;
import com.legendshop.product.dto.ShopCategoryTree;
import com.legendshop.product.service.ShopCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Feign调用实现
 *
 * @author legendshop
 */
@RestController
@Validated
@RequiredArgsConstructor
public class ShopCategoryApiImpl implements ShopCategoryApi {

	private final ShopCategoryService shopCategoryService;

	@Override
	public R<List<ShopCategoryTree>> getByShopIdAndStatus(Long shopId, Integer status) {
		// 获取符合条件的类目
		Set<ShopCategoryBO> all = new HashSet<>();
		all.addAll(shopCategoryService.queryByShopId(shopId, status));
		return R.ok(shopCategoryService.filterShopCategory(all));
	}
}
