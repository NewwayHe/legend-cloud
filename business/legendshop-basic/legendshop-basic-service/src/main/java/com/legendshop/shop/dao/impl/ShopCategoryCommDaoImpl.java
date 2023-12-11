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
import cn.legendshop.jpaplus.support.EntityCriterion;
import com.legendshop.shop.dao.ShopCategoryCommDao;
import com.legendshop.shop.entity.ShopCategoryComm;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 常用分类Dao
 *
 * @author legendshop
 */
@Repository
public class ShopCategoryCommDaoImpl extends GenericDaoImpl<ShopCategoryComm, Long> implements ShopCategoryCommDao {

	@Override
	public List<ShopCategoryComm> getShopCategoryCommListByShopId(Long shopId) {
		return this.query("SELECT co.id AS id,co.shop_id AS shopId,co.category_id AS categoryId,co.rec_date AS recDate FROM ls_category_comm co,ls_category ca WHERE co.category_id= ca.id AND co.shop_id = ? AND ca.status = 1 ORDER BY co.rec_date DESC;", ShopCategoryComm.class, shopId);
	}

	@Override
	public ShopCategoryComm getShopCategoryComm(Long categoryId, Long shopId) {
		return this.getByProperties(new EntityCriterion().eq("categoryId", categoryId).eq("shopId", shopId));
	}

}
