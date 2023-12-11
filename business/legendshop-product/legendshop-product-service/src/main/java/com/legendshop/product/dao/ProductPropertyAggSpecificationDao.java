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
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.entity.ProductPropertyAggSpecification;
import com.legendshop.product.query.ProductPropertyAggQuery;

import java.util.List;

/**
 * The Class ProductPropertyDao.
 *
 * @author legendshop
 */
public interface ProductPropertyAggSpecificationDao extends GenericDao<ProductPropertyAggSpecification, Long> {
	/**
	 * 根据aggId删除相关数据。
	 *
	 * @param aggId aggId
	 * @return 是否成功删除
	 */
	boolean deleteByAggId(Long aggId);
	/**
	 * 根据aggId获取关联的个数。
	 *
	 * @param aggId aggId
	 * @return 关联数量
	 */
	int getCountByAggId(Long aggId);

	/**
	 * 根据类目关联 ID 查询规格属性名称的方法。
	 *
	 * @param aggIdList 类目关联 ID 列表
	 * @return 包含规格属性名称的列表
	 */
	List<ProductPropertyBO> queryByAggId(List<Long> aggIdList);

	/**
	 * 根据属性 ID 查询类目绑定名称的方法。
	 *
	 * @param productIdList 属性 ID 列表
	 * @return 包含类目绑定名称的列表
	 */
	List<ProductPropertyBO> getAggNameByPropId(List<Long> productIdList);

	/**
	 * 根据查询条件分页查询属性的方法。
	 *
	 * @param query 属性查询对象
	 * @return 包含属性信息的分页支持对象
	 */
	PageSupport<ProductPropertyBO> queryByPage(ProductPropertyAggQuery query);

	/**
	 * 根据属性 ID 删除相关数据的方法。
	 *
	 * @param id 属性 ID
	 * @return 删除操作影响的行数
	 */
	int deleteByPropId(Long id);
}
