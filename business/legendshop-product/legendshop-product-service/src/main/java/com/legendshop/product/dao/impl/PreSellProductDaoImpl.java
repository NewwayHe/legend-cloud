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
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.product.dao.PreSellProductDao;
import com.legendshop.product.entity.PreSellProduct;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 预售商品表(PreSellProduct)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2020-08-18 10:14:15
 */
@Repository
public class PreSellProductDaoImpl extends GenericDaoImpl<PreSellProduct, Long> implements PreSellProductDao {

	@Override
	public PreSellProduct getByProductId(Long id) {
		return getByProperties(new LambdaEntityCriterion<>(PreSellProduct.class).eq(PreSellProduct::getProductId, id));
	}

	@Override
	public List<PreSellProduct> queryByProductId(Long productId) {
		return query("SELECT * from ls_pre_sell_product a where  a.product_id=?", PreSellProduct.class, productId);
	}
}
