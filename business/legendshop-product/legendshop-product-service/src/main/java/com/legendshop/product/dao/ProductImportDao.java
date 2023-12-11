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
import com.legendshop.common.core.constant.R;
import com.legendshop.product.entity.ProductImport;
import com.legendshop.product.query.ProductQuery;

/**
 * @author legendshop
 */
public interface ProductImportDao extends GenericDao<ProductImport, Long> {
	/**
	 * page
	 * @param query
	 * @return
	 */
	R<PageSupport<ProductImport>> page(ProductQuery query);
}
