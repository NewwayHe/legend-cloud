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
import com.legendshop.product.bo.ParamGroupBO;
import com.legendshop.product.bo.ProductPropertyAggParamGroupBO;
import com.legendshop.product.entity.ProductPropertyAggParamGroup;
import com.legendshop.product.query.ProductPropertyAggQuery;

import java.util.List;

/**
 * @author legendshop
 */
public interface ProductPropertyAggParamGroupDao extends GenericDao<ProductPropertyAggParamGroup, Long> {
	/**
	 * 根据aggId获取关联的个数。
	 *
	 * @param aggId  ID
	 * @return 关联数量
	 */
	int getCountByAggId(Long aggId);

	/**
	 * 根据aggId分页查询参数组
	 *
	 * @param query 参数组查询对象
	 * @return 包含参数组的分页支持对象
	 */
	PageSupport<ProductPropertyAggParamGroupBO> queryByPage(ProductPropertyAggQuery query);
	/**
	 * 根据aggId删除
	 *
	 * @param id  ID
	 * @return 是否成功删除
	 */
	boolean deleteByAggId(Long id);

	/**
	 * 根据aggId列表查询参数组。
	 *
	 * @param aggIdList ID 列表
	 * @return 包含参数组的列表
	 */
	List<ParamGroupBO> queryByAggId(List<Long> aggIdList);
	/**
	 * 根据类目id,查询所有的参数组
	 *
	 * @param id 类目 ID
	 * @return 包含参数组的列表
	 */
	List<ParamGroupBO> queryByCategoryId(long id);
}
