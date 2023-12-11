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
import com.legendshop.product.bo.ProductPropertyAggBO;
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.entity.ProductPropertyAggParam;
import com.legendshop.product.query.ParamGroupQuery;
import com.legendshop.product.query.ProductPropertyAggQuery;

import java.util.List;

/**
 * The Class ProductTypeParamDao.
 *
 * @author legendshop
 */
public interface ProductPropertyAggParamDao extends GenericDao<ProductPropertyAggParam, Long> {

	Boolean deleteByAggId(Long id);

	int getCountByAggId(Long aggId);

	/**
	 * 根据类目关联ID查询参数属性名称
	 *
	 * @param aggIdList 类目关联ID列表
	 * @return 参数属性名称列表
	 */
	List<ProductPropertyBO> queryByAggId(List<Long> aggIdList);

	/**
	 * 根据属性ID查询类目关联管理名称
	 *
	 * @param productIdList 属性ID列表
	 * @return 类目关联管理名称列表
	 */
	List<ProductPropertyBO> getAggNameByPropId(List<Long> productIdList);

	/**
	 * 根据参数组ID集合查询类目关联管理名称
	 *
	 * @param groupIds 参数组ID集合
	 * @return 类目关联管理名称列表
	 */
	List<ProductPropertyAggBO> queryAggNameByGroupId(List<Long> groupIds);

	/**
	 * 分页查询
	 *
	 * @param query 查询条件
	 * @return 分页结果
	 */
	PageSupport<ProductPropertyBO> queryByPage(ProductPropertyAggQuery query);

	/**
	 * 根据属性ID删除
	 *
	 * @param id 属性ID
	 */
	void deleteByPropId(Long id);

	/**
	 * 根据参数组ID查询参数和值
	 *
	 * @param query 参数组查询条件
	 * @return 分页结果包含参数和值
	 */
	PageSupport<ProductPropertyBO> getParamAndValueById(ParamGroupQuery query);

}
