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
import com.legendshop.product.entity.ProductSnapshot;

/**
 * 商品快照.
 *
 * @author legendshop
 */
public interface ProductSnapshotDao extends GenericDao<ProductSnapshot, Long> {

	ProductSnapshot getProductSnapshot(Long id);

	Long saveProductSnapshot(ProductSnapshot productSnapshot);

	ProductSnapshot getProductSnapshot(Long productId, Long skuId, Integer version);

}
