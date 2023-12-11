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
import com.legendshop.product.dao.ProductPropertyImageDao;
import com.legendshop.product.entity.ProductPropertyImage;
import org.springframework.stereotype.Repository;

/**
 * 属性图片Dao.
 *
 * @author legendshop
 */
@Repository
public class ProductPropertyImageDaoImpl extends GenericDaoImpl<ProductPropertyImage, Long> implements ProductPropertyImageDao {

	@Override
	public int deleteByProductId(Long productId) {
		return this.update("delete from ls_product_property_image where product_id = ?", productId);
	}
}
