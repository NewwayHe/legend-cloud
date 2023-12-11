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
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.bo.CategoryBO;
import com.legendshop.product.bo.ProductCategoryRelationBO;
import com.legendshop.product.entity.Category;
import com.legendshop.product.query.ProductCategoryRelationQuery;

import java.util.List;

/**
 * 分类树Dao
 *
 * @author legendshop
 */
public interface CategoryDao extends GenericDao<Category, Long> {

	String getCategoryNameById(Long id);

	int updateStatus(int status, Long id);

	/**
	 * 根据父级id查询类目
	 *
	 * @param parentId
	 * @param status   上线状态【0：下线，1：上线，2：全部】
	 * @return
	 */
	List<CategoryBO> queryByParentId(Long parentId, int status);

	/**
	 * 根据父级id和名称查询
	 *
	 * @param parentId
	 * @param name
	 * @param status
	 * @return
	 */
	List<Category> queryByParentIdAndName(Long parentId, String name, int status);

	CategoryBO getCategoryBOById(Long aLong);

	CategoryBO queryByIdName(String name);

	List<CategoryBO> queryAllOnline();

	/**
	 * 根据类目id查询类目下的商品列表
	 *
	 * @param relationQuery
	 * @return
	 */
	PageSupport<ProductCategoryRelationBO> queryProductList(ProductCategoryRelationQuery relationQuery);

	/**
	 * 获取在线类目，除了被其它参数组使用的
	 *
	 * @return
	 */
	List<CategoryBO> queryAllOnlineByProductPropertyAggId();

	/**
	 * 根据类目ID 查询类目下所有商品信息
	 *
	 * @param id
	 * @return
	 */
	List<Long> queryProductIdsListById(Long id);
}
