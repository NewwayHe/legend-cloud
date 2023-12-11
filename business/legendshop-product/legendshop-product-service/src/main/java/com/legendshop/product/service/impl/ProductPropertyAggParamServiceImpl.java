/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import com.legendshop.product.dao.ProductPropertyAggParamDao;
import com.legendshop.product.dto.ProductPropertyAggBatchDTO;
import com.legendshop.product.entity.ProductPropertyAggParam;
import com.legendshop.product.service.ProductPropertyAggParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 类目关联跟参数的关系服务
 *
 * @author legendshop
 */
@Service
public class ProductPropertyAggParamServiceImpl implements ProductPropertyAggParamService {

	@Autowired
	private ProductPropertyAggParamDao aggParamDao;

	@Override
	public int deleteById(Long id) {
		return aggParamDao.deleteById(id);
	}

	@Override
	public boolean save(ProductPropertyAggBatchDTO aggDTO) {

		List<ProductPropertyAggParam> list = new ArrayList<>();

		for (int i = 0; i < aggDTO.getAggIdlist().size(); i++) {
			//默认排序排在最后
			int count = aggParamDao.getCountByAggId(aggDTO.getAggIdlist().get(i));
			ProductPropertyAggParam aggSpecification = new ProductPropertyAggParam(aggDTO.getAggIdlist().get(i), aggDTO.getPropId(), count + 1);
			list.add(aggSpecification);
		}
		return aggParamDao.save(list).size() > 0;

	}

	@Override
	public void deleteByPropId(Long id) {
		aggParamDao.deleteByPropId(id);
	}
}
