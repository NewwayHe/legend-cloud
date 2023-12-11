/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import com.legendshop.product.dao.ProductPropertyAggSpecificationDao;
import com.legendshop.product.dto.ProductPropertyAggBatchDTO;
import com.legendshop.product.entity.ProductPropertyAggSpecification;
import com.legendshop.product.service.ProductPropertyAggSpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 类目关联跟属性的关系服务
 *
 * @author legendshop
 */
@Service
public class ProductPropertyAggSpecificationServiceImpl implements ProductPropertyAggSpecificationService {

	@Autowired
	private ProductPropertyAggSpecificationDao aggSpecificationDao;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean save(ProductPropertyAggBatchDTO dto) {
		List<ProductPropertyAggSpecification> list = new ArrayList<>();

		for (int i = 0; i < dto.getAggIdlist().size(); i++) {
			//默认排序排在最后
			int count = aggSpecificationDao.getCountByAggId(dto.getAggIdlist().get(i));
			ProductPropertyAggSpecification aggSpecification = new ProductPropertyAggSpecification(dto.getAggIdlist().get(i), dto.getPropId(), count + 1);
			list.add(aggSpecification);
		}
		return aggSpecificationDao.save(list).size() > 0;

	}

	@Override
	public int deleteByProductId(Long productId) {
		return aggSpecificationDao.deleteByPropId(productId);
	}

	@Override
	public int deleteById(Long id) {
		return aggSpecificationDao.deleteById(id);
	}

}
