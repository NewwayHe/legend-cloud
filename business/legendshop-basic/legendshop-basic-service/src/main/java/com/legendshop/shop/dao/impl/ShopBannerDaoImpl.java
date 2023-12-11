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
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.shop.dao.ShopBannerDao;
import com.legendshop.shop.entity.ShopBanner;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商家默认的轮换图Dao
 *
 * @author legendshop
 */
@Repository
public class ShopBannerDaoImpl extends GenericDaoImpl<ShopBanner, Long> implements ShopBannerDao {

	@Override
	public List<ShopBanner> getShopBannerByShopId(Long shopId) {
		List<ShopBanner> banners = queryByProperties(new EntityCriterion().eq("shopId", shopId).addAscOrder("seq"));
		return banners;
	}

	@Override
	public PageSupport<ShopBanner> getShopBanner(String curPageNo, Long shopId) {
		CriteriaQuery cq = new CriteriaQuery(ShopBanner.class, curPageNo);
		cq.eq("shopId", shopId);
		cq.addAscOrder("seq");
		return queryPage(cq);
	}

}
