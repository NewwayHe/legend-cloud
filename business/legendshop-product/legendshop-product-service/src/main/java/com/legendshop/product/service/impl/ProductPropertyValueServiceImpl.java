/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import com.legendshop.common.core.constant.R;
import com.legendshop.product.bo.ProductPropertyValueBO;
import com.legendshop.product.dao.ProductPropertyValueDao;
import com.legendshop.product.dto.ProductPropertyValueDTO;
import com.legendshop.product.service.ProductPropertyValueService;
import com.legendshop.product.service.convert.ProductPropertyValueConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品属性值服务.
 *
 * @author legendshop
 */
@Service
public class ProductPropertyValueServiceImpl implements ProductPropertyValueService {

	@Autowired
	private ProductPropertyValueDao productPropertyValueDao;

	@Autowired
	private ProductPropertyValueConverter productPropertyValueConverter;

	@Override
	public List<ProductPropertyValueBO> getProductPropertyValue(List<Long> valueIdList) {
		return productPropertyValueConverter.convert2BoList(productPropertyValueDao.getProductPropertyValue(valueIdList));
	}

	@Override
	public boolean saveWithId(List<ProductPropertyValueDTO> productPropertyValueList) {
		return productPropertyValueDao.saveWithId(productPropertyValueConverter.from(productPropertyValueList)).size() == productPropertyValueList.size();
	}

	@Override
	public R save(ProductPropertyValueDTO s) {
		return R.ok(productPropertyValueDao.save(productPropertyValueConverter.from(s)));
	}

	@Override
	public Long createId() {
		return productPropertyValueDao.createId();
	}

}
