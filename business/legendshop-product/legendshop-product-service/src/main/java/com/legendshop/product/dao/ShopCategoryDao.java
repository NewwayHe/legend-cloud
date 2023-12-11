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
import com.legendshop.product.bo.ShopCategoryBO;
import com.legendshop.product.entity.ShopCategory;

import java.util.List;

/**
 * 商家的商品类目Dao
 *
 * @author legendshop
 */
public interface ShopCategoryDao extends GenericDao<ShopCategory, Long> {

	/**
	 * 根据 ParentId 查找分类
	 **/
	List<ShopCategory> queryByParentId(Long nextCatId);

	/**
	 * 根据 ShopId 查找分类,状态 1上线，2下线，3全部
	 **/
	List<ShopCategoryBO> queryByShopId(Long shopId, int status);

	int updateStatus(Integer status, Long id);
}
