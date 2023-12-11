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
import com.legendshop.product.bo.ProductGroupRelationBO;
import com.legendshop.product.entity.ProductGroup;
import com.legendshop.product.query.ProductGroupQuery;
import com.legendshop.product.query.ProductGroupRelationQuery;

/**
 * The Class ProductGroupDao. Dao接口
 *
 * @author legendshop
 */
public interface ProductGroupDao extends GenericDao<ProductGroup, Long> {

	PageSupport<ProductGroup> queryProductGroupPage(ProductGroupQuery productGroupQuery);

	PageSupport<ProductGroup> page(ProductGroupQuery productGroupQuery);

	/**
	 * 查看分组下的商品列表
	 *
	 * @param relationQuery
	 * @return
	 */
	PageSupport<ProductGroupRelationBO> queryProductList(ProductGroupRelationQuery relationQuery);

}
