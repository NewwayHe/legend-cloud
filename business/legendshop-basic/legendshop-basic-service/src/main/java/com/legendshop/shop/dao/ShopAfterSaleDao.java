/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.shop.entity.ShopAfterSale;
import com.legendshop.shop.query.ShopAfterSaleQuery;

/**
 * @author legendshop
 */
public interface ShopAfterSaleDao extends GenericDao<ShopAfterSale, Long> {

	PageSupport<ShopAfterSale> getShopAfterSale(ShopAfterSaleQuery shopAfterSaleQuery);

}
