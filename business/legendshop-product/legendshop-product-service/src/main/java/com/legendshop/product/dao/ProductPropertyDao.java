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
import com.legendshop.product.dto.ProductPropertyDTO;
import com.legendshop.product.entity.ProductProperty;
import com.legendshop.product.query.ProductPropertyQuery;

import java.util.List;

/**
 * 商品属性Dao.
 *
 * @author legendshop
 */
public interface ProductPropertyDao extends GenericDao<ProductProperty, Long> {

	List<ProductProperty> getProductProperty(String propName);

	ProductProperty getProperty(String propName, String type, Long shopId);

	List<ProductProperty> getProductPropertyList(Long proTypeId);

	/**
	 * 根据aggId查询所有的参数属性。
	 *
	 * @param aggIdList aggId列表
	 * @return 包含参数属性的列表
	 */
	List<ProductProperty> getParameterPropertyList(List<Long> aggIdList);

	/**
	 * 根据aggId查询所有的规格属性。
	 *
	 * @param aggIdList aggId列表
	 * @return 包含规格属性的列表
	 */
	List<ProductProperty> getSpecificationPropertyList(List<Long> aggIdList);


	List<ProductProperty> getProductProperty(List<Long> propIds, Long categoryId);

	/**
	 * 直接为自定义属性 获取id
	 **/
	Long saveCustomProperty(ProductProperty newProperty);

	/**
	 * 根据 商品ID 查找商品自定义属性
	 **/
	List<ProductPropertyDTO> queryUserPropByProductId(Long productId);

	PageSupport<ProductProperty> getProductPropertyPage(ProductPropertyQuery productPropertyQuery);

	PageSupport<ProductProperty> getProductPropertyParamPage(ProductPropertyQuery productPropertyQuery);

	PageSupport<ProductProperty> queryProductPropertyPage(ProductPropertyQuery productPropertyQuery);

	/**
	 * 根据规格id查找对应的商品id
	 */
	Integer findProductId(Long propId);

	int updateDeleteFlag(Long id, Boolean deleteFlag);

	List<String> getPropertyValueList(Long id);

	/**
	 * 根据图片路径查找图片
	 *
	 * @param url
	 * @return
	 */
	List<String> queryAttachmentByUrl(String url);
}
