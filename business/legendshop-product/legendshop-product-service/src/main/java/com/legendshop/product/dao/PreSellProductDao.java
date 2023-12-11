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
import com.legendshop.product.entity.PreSellProduct;

import java.util.List;

/**
 * 预售商品表(PreSellProduct)表数据库访问层
 *
 * @author legendshop
 */
public interface PreSellProductDao extends GenericDao<PreSellProduct, Long> {

	/**
	 * 根据商品id查询预售信息
	 */
	PreSellProduct getByProductId(Long id);

	/**
	 * 根据商品id获取预售信息
	 */
	List<PreSellProduct> queryByProductId(Long productId);
}
