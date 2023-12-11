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
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.service.ProductPropertyAggService;
import com.legendshop.product.service.ProductPropertyService;
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
public class ProductPropertyApiImpl implements ProductPropertyApi {

	final ProductPropertyService productPropertyService;
	final ProductPropertyAggService productPropertyAggService;

	@Override
	public R<List<ProductPropertyBO>> queryParamByCategoryId(Long categoryId, Long productId) {
		return R.ok(productPropertyService.queryParamByCategoryId(categoryId, productId));
	}

	@Override
	public R<List<ProductPropertyBO>> queryParamByCategoryIds(List<Long> categoryId, Long productId) {
		return R.ok(productPropertyService.queryParamByCategoryIds(categoryId, productId));
	}

	@Override
	public R<List<String>> queryAttachmentByUrl(String url) {
		return R.ok(productPropertyService.queryAttachmentByUrl(url));
	}
}
