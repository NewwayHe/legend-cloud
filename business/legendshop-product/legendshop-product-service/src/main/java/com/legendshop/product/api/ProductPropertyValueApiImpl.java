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
import com.legendshop.product.bo.ProductPropertyValueBO;
import com.legendshop.product.service.ProductPropertyValueService;
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
public class ProductPropertyValueApiImpl implements ProductPropertyValueApi {

	final ProductPropertyValueService productPropertyValueService;

	@Override
	public R<List<ProductPropertyValueBO>> getProductPropertyValue(List<Long> valueIdList) {
		return R.ok(productPropertyValueService.getProductPropertyValue(valueIdList));
	}
}
