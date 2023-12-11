/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.shop.dao.ShopAfterSaleDao;
import com.legendshop.shop.entity.ShopAfterSale;
import com.legendshop.shop.query.ShopAfterSaleQuery;
import org.springframework.stereotype.Repository;

/**
 * 售后服务说明表
 *
 * @author legendshop
 */
@Repository
public class ShopAfterSaleDaoImpl extends GenericDaoImpl<ShopAfterSale, Long> implements ShopAfterSaleDao {

	@Override
	public PageSupport<ShopAfterSale> getShopAfterSale(ShopAfterSaleQuery shopAfterSaleQuery) {
		CriteriaQuery cq = new CriteriaQuery(ShopAfterSale.class, shopAfterSaleQuery.getPageSize(), shopAfterSaleQuery.getCurPage());
		cq.eq("userName", shopAfterSaleQuery.getUserName());
		cq.eq("userId", shopAfterSaleQuery.getUserId());
		cq.setPageSize(3);
		cq.addDescOrder("id");
		return queryPage(cq);
	}

}
