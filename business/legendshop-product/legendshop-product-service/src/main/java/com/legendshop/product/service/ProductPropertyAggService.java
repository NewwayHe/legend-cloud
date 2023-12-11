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
import com.legendshop.product.bo.BrandBO;
import com.legendshop.product.bo.ProductPropertyAggBO;
import com.legendshop.product.bo.ProductPropertyAggParamGroupBO;
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.dto.ProductPropertyAggDTO;
import com.legendshop.product.query.ProductPropertyAggQuery;

import java.util.List;

/**
 * 商品类型Service.
 *
 * @author legendshop
 */
public interface ProductPropertyAggService {

	ProductPropertyAggBO getById(Long id);

	ProductPropertyAggBO getDetailById(Long id);

	int deleteById(Long id);

	Long save(ProductPropertyAggDTO productType);

	int update(ProductPropertyAggDTO productType);

	/**
	 * 根据名称简单分页查询
	 */
	PageSupport<ProductPropertyAggBO> querySimplePage(ProductPropertyAggQuery query);

	/**
	 * 根据名称查询详细信息
	 */
	PageSupport<ProductPropertyAggBO> queryPage(ProductPropertyAggQuery productPropertyAggQuery);


	PageSupport<ProductPropertyBO> getSpecificationById(ProductPropertyAggQuery productPropertyAggQuery);

	PageSupport<ProductPropertyBO> getParamById(ProductPropertyAggQuery query);

	PageSupport<BrandBO> getBrandById(ProductPropertyAggQuery query);

	PageSupport<BrandBO> getBrandByIds(ProductPropertyAggQuery query);

	PageSupport<ProductPropertyAggParamGroupBO> getParamGroupById(ProductPropertyAggQuery query);

	List<ProductPropertyAggDTO> listAll();
}
