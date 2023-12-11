/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import com.legendshop.product.dao.ProductPropertyAggBrandDao;
import com.legendshop.product.dto.BrandDTO;
import com.legendshop.product.service.ProductPropertyAggBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类目关联跟品牌的关系服务
 *
 * @author legendshop
 */
@Service
public class ProductPropertyAggBrandServiceImpl implements ProductPropertyAggBrandService {

	@Autowired
	private ProductPropertyAggBrandDao aggBrandDao;

	@Override
	public int deleteById(Long id) {
		return aggBrandDao.deleteById(id);
	}

	@Override
	public List<BrandDTO> queryBrandListByCategory(Long id) {
		return aggBrandDao.queryBrandListByCategory(id);
	}

}
