/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import com.legendshop.product.dao.ProductPropertyAggParamGroupDao;
import com.legendshop.product.dto.ProductPropertyAggBatchDTO;
import com.legendshop.product.entity.ProductPropertyAggParamGroup;
import com.legendshop.product.service.ProductPropertyAggParamGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 类目关联跟参数组的关系服务
 *
 * @author legendshop
 */
@Service
public class ProductPropertyAggParamGroupServiceImpl implements ProductPropertyAggParamGroupService {

	@Autowired
	private ProductPropertyAggParamGroupDao paramGroupDao;

	@Override
	public boolean save(ProductPropertyAggBatchDTO aggDTO) {
		List<ProductPropertyAggParamGroup> list = new ArrayList<>();

		for (int i = 0; i < aggDTO.getAggIdlist().size(); i++) {
			//默认排序排在最后
			int count = paramGroupDao.getCountByAggId(aggDTO.getAggIdlist().get(i));
			ProductPropertyAggParamGroup aggParamGroup = new ProductPropertyAggParamGroup(aggDTO.getAggIdlist().get(i), aggDTO.getParamGroupId(), count + 1);
			list.add(aggParamGroup);
		}
		return paramGroupDao.save(list).size() > 0;
	}

	@Override
	public int deleteById(Long id) {
		return paramGroupDao.deleteById(id);
	}

}
