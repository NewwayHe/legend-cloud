/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.lambda.LambdaCriteriaQuery;
import com.legendshop.common.core.constant.R;
import com.legendshop.product.dao.ProductImportDao;
import com.legendshop.product.entity.ProductImport;
import com.legendshop.product.query.ProductQuery;
import org.springframework.stereotype.Repository;

/**
 * @author legendshop
 */
@Repository
public class ProductImportDaoImpl extends GenericDaoImpl<ProductImport, Long> implements ProductImportDao {


	@Override
	public R<PageSupport<ProductImport>> page(ProductQuery query) {
		LambdaCriteriaQuery<ProductImport> cq = new LambdaCriteriaQuery<>(ProductImport.class, query);
		cq.eq(ProductImport::getShopId, query.getShopId());
		if (ObjectUtil.isNotEmpty(query.getStartDate())) {
			cq.ge(ProductImport::getCreateTime, DateUtil.beginOfDay(query.getStartDate()));
		}
		if (ObjectUtil.isNotEmpty(query.getEndDate())) {
			cq.le(ProductImport::getCreateTime, DateUtil.endOfDay(query.getEndDate()));
		}
		cq.addDescOrder(ProductImport::getCreateTime);
		return R.ok(this.queryPage(cq));
	}
}
