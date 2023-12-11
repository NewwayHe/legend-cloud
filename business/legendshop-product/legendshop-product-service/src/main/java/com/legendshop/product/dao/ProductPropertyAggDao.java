/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.bo.ProductPropertyAggBO;
import com.legendshop.product.entity.ProductPropertyAgg;
import com.legendshop.product.query.ProductPropertyAggQuery;

import java.util.List;

/**
 * 商品类型Dao
 *
 * @author legendshop
 */
public interface ProductPropertyAggDao extends GenericDao<ProductPropertyAgg, Long> {

	PageSupport<ProductPropertyAgg> queryProductTypePage(ProductPropertyAggQuery productPropertyAggQuery);

	/**
	 * 查询未关联的类目关联
	 */
	PageSupport<ProductPropertyAgg> querySimplePage(ProductPropertyAggQuery query);

	/**
	 * 根据属性id查询类目关联
	 */
	List<ProductPropertyAggBO> queryByProductId(Long id);

	List<ProductPropertyAgg> listAll();

	Long getId(String name);

	Long getCategoryId(Long categoryId);
}
