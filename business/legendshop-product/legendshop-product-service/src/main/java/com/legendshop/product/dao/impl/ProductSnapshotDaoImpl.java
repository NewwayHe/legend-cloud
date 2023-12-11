/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import com.legendshop.product.dao.ProductSnapshotDao;
import com.legendshop.product.entity.ProductSnapshot;
import org.springframework.stereotype.Repository;

/**
 * The Class ProductSnapshotDaoImpl.
 *
 * @author legendshop
 */
@Repository
public class ProductSnapshotDaoImpl extends GenericDaoImpl<ProductSnapshot, Long> implements ProductSnapshotDao {


	@Override
	public ProductSnapshot getProductSnapshot(Long id) {
		return getById(id);
	}

	@Override
	public Long saveProductSnapshot(ProductSnapshot productSnapshot) {
		return save(productSnapshot);
	}

	@Override
	public ProductSnapshot getProductSnapshot(Long productId, Long skuId, Integer version) {
		return this.getByProperties(new EntityCriterion().eq("productId", productId).eq("skuId", skuId).eq("version", version));
	}

}
