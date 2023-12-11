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
import com.legendshop.product.bo.ProductPropertyAggCategoryBO;
import com.legendshop.product.entity.ProductPropertyAggCategory;

import java.util.List;

/**
 * @author legendshop
 */
public interface ProductPropertyAggCategoryDao extends GenericDao<ProductPropertyAggCategory, Long> {

	/**
	 * 根据aggID删除数据的方法。
	 *
	 * @param id 聚合ID
	 * @return 删除操作是否成功的布尔值
	 */
	boolean deleteByAggId(Long id);

	/**
	 * 根据aggID列表查询属性聚合类目的方法。
	 *
	 * @param aggIdList 聚合ID列表
	 * @return 属性聚合类目列表
	 */
	List<ProductPropertyAggCategoryBO> queryByAggId(List<Long> aggIdList);

	/**
	 * 根据类目ID查询关联的aggID的方法。
	 *
	 * @param categoryId 类目ID
	 * @return 类目关联的聚合ID
	 */
	Long queryAggIdByCategoryId(Long categoryId);

	/**
	 * 根据类目ID获取属性聚合类目信息的方法。
	 *
	 * @param categoryId 类目ID
	 * @return 属性聚合类目对象
	 */
	ProductPropertyAggCategory getByCategoryId(Long categoryId);

	/**
	 * 根据类目ID列表删除数据的方法。
	 *
	 * @param categoryIds 类目ID列表
	 */
	void deleteByCategoryId(List<Long> categoryIds);

}
