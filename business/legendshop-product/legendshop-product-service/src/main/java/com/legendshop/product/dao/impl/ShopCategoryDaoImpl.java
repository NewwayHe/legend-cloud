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
import com.legendshop.product.bo.ShopCategoryBO;
import com.legendshop.product.dao.ShopCategoryDao;
import com.legendshop.product.entity.ShopCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商家的商品类目Dao
 *
 * @author legendshop
 */
@Repository
public class ShopCategoryDaoImpl extends GenericDaoImpl<ShopCategory, Long> implements ShopCategoryDao {

	@Override
	public List<ShopCategory> queryByParentId(Long nextCatId) {
		return queryByProperties(new EntityCriterion().eq("parentId", nextCatId));
	}

	@Override
	public List<ShopCategoryBO> queryByShopId(Long shopId, int status) {
		return query(getSQL("ShopCat.queryByShopId"), ShopCategoryBO.class, shopId, status, status);
	}

	@Override
	public int updateStatus(Integer status, Long id) {
		return update("update ls_shop_cat set status=?,update_time=NOW() where id=? ", status, id);
	}
}
