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
import com.legendshop.product.bo.BrandBO;
import com.legendshop.product.dto.BrandDTO;
import com.legendshop.product.entity.ProductPropertyAggBrand;
import com.legendshop.product.query.ProductPropertyAggQuery;

import java.util.List;

/**
 * The Class ProductTypeBrandDao.
 *
 * @author legendshop
 */
public interface ProductPropertyAggBrandDao extends GenericDao<ProductPropertyAggBrand, Long> {

	/**
	 * 根据聚合ID删除记录
	 *
	 * @param id 聚合ID
	 * @return 删除操作结果（成功/失败）
	 */
	boolean deleteByAggId(Long id);

	/**
	 * 根据类目关联ID查询品牌名称列表
	 *
	 * @param aggIdList 类目关联ID列表
	 * @return 品牌信息列表
	 */
	List<BrandBO> queryBrandByAggId(List<Long> aggIdList);

	/**
	 * 分页查询产品属性聚合信息
	 *
	 * @param query 产品属性聚合查询条件
	 * @return 分页结果
	 */
	PageSupport<BrandBO> queryByPage(ProductPropertyAggQuery query);

	/**
	 * 根据类目ID查找关联的品牌列表
	 *
	 * @param id 类目ID
	 * @return 品牌列表
	 */
	List<BrandDTO> queryBrandListByCategory(Long id);

	/**
	 * 根据ID查询品牌列表中的品牌数量
	 *
	 * @param id ID
	 * @return 品牌数量
	 */
	Long queryBrandListBrand(Long id);

}
