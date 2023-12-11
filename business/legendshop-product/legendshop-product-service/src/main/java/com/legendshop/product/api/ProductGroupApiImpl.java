/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.api;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.product.bo.ProductGroupBO;
import com.legendshop.product.bo.ProductGroupRelationBO;
import com.legendshop.product.query.ProductGroupQuery;
import com.legendshop.product.query.ProductGroupRelationQuery;
import com.legendshop.product.service.ProductGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

/**
 * Feign调用实现
 *
 * @author legendshop
 */
@RestController
@Validated
@RequiredArgsConstructor
public class ProductGroupApiImpl implements ProductGroupApi {

	final ProductGroupService productGroupService;

	@Override
	public R<ProductGroupBO> getProductGroup(Long id) {
		return R.ok(productGroupService.getProductGroup(id));
	}

	@Override
	public R<PageSupport<ProductGroupBO>> queryProductGroupListPage(ProductGroupQuery productGroupQuery) {
		return R.ok(productGroupService.queryProductGroupListPage(productGroupQuery));
	}

	@Override
	public R<PageSupport<ProductGroupRelationBO>> queryProductList(ProductGroupRelationQuery relationQuery) {
		return R.ok(productGroupService.queryProductList(relationQuery));
	}
}
