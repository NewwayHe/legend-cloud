/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import com.legendshop.common.core.constant.R;
import com.legendshop.product.bo.ProductPropertyValueBO;
import com.legendshop.product.dto.ProductPropertyValueDTO;

import java.util.List;

/**
 * 商品属性值
 *
 * @author legendshop
 */
public interface ProductPropertyValueService {

	List<ProductPropertyValueBO> getProductPropertyValue(List<Long> valueIdList);

	boolean saveWithId(List<ProductPropertyValueDTO> productPropertyValueList);

	R save(ProductPropertyValueDTO s);

	Long createId();
}
