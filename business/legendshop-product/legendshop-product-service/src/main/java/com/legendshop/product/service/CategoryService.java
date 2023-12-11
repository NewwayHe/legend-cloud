/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.product.bo.CategoryBO;
import com.legendshop.product.bo.ProductCategoryRelationBO;
import com.legendshop.product.dto.CategoryDTO;
import com.legendshop.product.dto.CategoryTree;
import com.legendshop.product.entity.Category;
import com.legendshop.product.query.ProductCategoryRelationQuery;

import java.util.List;
import java.util.Set;

/**
 * 商品分类
 *
 * @author legendshop
 */
public interface CategoryService {

	/**
	 * 根据ID获取分类
	 *
	 * @param id
	 * @return
	 */
	CategoryBO getById(Long id);

	/**
	 * 根据ID获取分类名称
	 *
	 * @param id
	 * @return
	 */
	String getCategoryNameById(Long id);

	/**
	 * 根据id删除分类
	 *
	 * @param id
	 * @return
	 */
	R deleteById(Long id);

	/**
	 * 保存分类
	 *
	 * @param categoryDTO
	 * @return
	 */
	Long save(CategoryDTO categoryDTO);

	/**
	 * 更新分类
	 *
	 * @param categoryDTO
	 * @return
	 */
	int update(CategoryDTO categoryDTO);

	/**
	 * 根据ID集合获取分类
	 *
	 * @param catIdList
	 * @return
	 */
	List<CategoryBO> getCategoryByIds(List<Long> catIdList);

	/**
	 * 更新状态
	 *
	 * @param status
	 * @param id
	 * @param includeProduct 是否影响到商品
	 * @return
	 */
	R updateStatus(int status, Long id, Boolean includeProduct);

	/**
	 * 根据父级id查询类目
	 *
	 * @param parentId
	 * @param status   上线状态【0：下线，1：上线，2：全部】
	 * @return
	 */
	List<CategoryBO> queryByParentId(Long parentId, int status);

	/**
	 * 查询所有在线的类目列表
	 *
	 * @return
	 */
	List<CategoryBO> queryAllOnline();

	/**
	 * 查询所有的类目列表
	 *
	 * @return
	 */
	List<CategoryBO> queryAll();

	/**
	 * 过滤生成类目树
	 *
	 * @param all
	 * @param parentId
	 * @return
	 */
	List<CategoryTree> filterCategory(Set<CategoryBO> all, Long parentId);

	/**
	 * 装修获取平台分类
	 *
	 * @return
	 */
	R<String> getDecorateCategoryList();

	R<List<CategoryTree>> getTreeById(Long productCategoryRoot);


	/**
	 * 根据父级id和名称查询
	 *
	 * @param parentId
	 * @param name
	 * @param status   0：下线，1：上线，2：全部
	 * @return
	 */
	List<CategoryBO> queryByParentIdAndName(Long parentId, String name, int status);

	/**
	 * 查看类目下的商品列表
	 *
	 * @param relationQuery
	 * @return
	 */
	PageSupport<ProductCategoryRelationBO> queryProductList(ProductCategoryRelationQuery relationQuery);

	/**
	 * 获取在线类目，除了被其它参数组使用的
	 *
	 * @param productPropertyAggId
	 * @return
	 */
	List<CategoryBO> queryAllOnlineByProductPropertyAggId(Long productPropertyAggId);

	R<Boolean> updateStatusBefore(Long id);

	Category queryId(Long categoryId);
}
