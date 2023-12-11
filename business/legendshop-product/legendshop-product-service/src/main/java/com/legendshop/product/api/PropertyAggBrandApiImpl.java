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
import com.legendshop.product.bo.BrandBO;
import com.legendshop.product.query.ProductPropertyAggQuery;
import com.legendshop.product.service.ProductPropertyAggService;
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
public class PropertyAggBrandApiImpl implements PropertyAggBrandApi {


	private final ProductPropertyAggService aggService;


	@Override
	public R<PageSupport<BrandBO>> getBrandByIds(Long id) {
		ProductPropertyAggQuery query = new ProductPropertyAggQuery();
		query.setCategoryId(id);
		return R.ok(aggService.getBrandByIds(query));
	}
}
