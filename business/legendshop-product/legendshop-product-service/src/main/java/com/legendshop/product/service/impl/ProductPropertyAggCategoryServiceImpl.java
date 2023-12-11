/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import com.legendshop.product.dao.ProductPropertyAggCategoryDao;
import com.legendshop.product.entity.ProductPropertyAggCategory;
import com.legendshop.product.service.ProductPropertyAggCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 类目关联跟类目的关系服务
 *
 * @author legendshop
 */
@Service
public class ProductPropertyAggCategoryServiceImpl implements ProductPropertyAggCategoryService {

	@Autowired
	private ProductPropertyAggCategoryDao aggCategoryDao;

	@Override
	public int deleteById(Long id) {
		return aggCategoryDao.deleteById(id);
	}

	@Override
	public void save(Long aggId, Long categoryId) {
		ProductPropertyAggCategory aggCategory = aggCategoryDao.getByCategoryId(categoryId);
		if (null != aggCategory) {
			aggCategory.setAggId(aggId);
			aggCategoryDao.update(aggCategory);
		} else {
			aggCategoryDao.save(new ProductPropertyAggCategory(aggId, categoryId, 1));
		}
	}

	@Override
	public void deleteByCategoryId(Long id) {
		aggCategoryDao.update("delete FROM ls_product_property_agg_category where category_id=? ", id);
	}

}
