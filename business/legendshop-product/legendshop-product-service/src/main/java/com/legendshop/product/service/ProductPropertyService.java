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
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.dto.ProductPropertyDTO;
import com.legendshop.product.query.ProductPropertyQuery;

import java.util.List;

/**
 * 商品属性服务
 *
 * @author legendshop
 */
public interface ProductPropertyService {
	/**
	 * 根据属性ID查询基本信息（包括属性值字符串）
	 *
	 * @param id 属性ID
	 * @return 包含基本信息的产品属性业务对象
	 */
	ProductPropertyBO getById(Long id);

	/**
	 * 根据属性ID查询详细信息（包括属性值列表）
	 *
	 * @param id 属性ID
	 * @return 包含详细信息的产品属性业务对象
	 */
	ProductPropertyBO getDetailById(Long id);

	/**
	 * 根据属性ID列表和类目ID查询相关信息
	 *
	 * @param propIds    属性ID列表
	 * @param categoryId 类目ID
	 * @return 产品属性业务对象列表
	 */
	List<ProductPropertyBO> getById(List<Long> propIds, Long categoryId);

	/**
	 * 根据类目ID和商品ID查询相关联的参数属性
	 *
	 * @param categoryId 类目ID
	 * @param productId  商品ID
	 * @return 相关联的参数属性列表
	 */
	List<ProductPropertyBO> queryParamByCategoryId(Long categoryId, Long productId);

	/**
	 * 根据多个类目ID和商品ID查询相关联的参数属性
	 *
	 * @param categoryIds 类目ID列表
	 * @param productId   商品ID
	 * @return 相关联的参数属性列表
	 */
	List<ProductPropertyBO> queryParamByCategoryIds(List<Long> categoryIds, Long productId);

	/**
	 * 根据类目ID和商品ID查询相关联的规格属性
	 *
	 * @param categoryId 类目ID
	 * @param productId  商品ID
	 * @return 相关联的规格属性列表
	 */
	List<ProductPropertyDTO> querySpecificationByCategoryId(Long categoryId, Long productId);

	/**
	 * 根据属性ID删除
	 *
	 * @param id 属性ID
	 */
	void deleteById(Long id);

	/**
	 * 更新产品属性信息
	 *
	 * @param productProperty 待更新的产品属性对象
	 * @return 更新操作结果（成功/失败）
	 */
	boolean update(ProductPropertyDTO productProperty);

	/**
	 * 分页查询产品属性信息
	 *
	 * @param productPropertyQuery 查询条件
	 * @return 分页结果
	 */
	PageSupport<ProductPropertyBO> queryPage(ProductPropertyQuery productPropertyQuery);

	/**
	 * 保存产品属性信息
	 *
	 * @param productPropertyDTO 待保存的产品属性对象
	 * @return 保存操作结果（成功/失败）
	 */
	boolean save(ProductPropertyDTO productPropertyDTO);

	/**
	 * 根据参数组ID查询属性
	 *
	 * @param id 参数组ID
	 * @return 属性列表
	 */
	List<ProductPropertyBO> queryByGroupId(Long id);

	/**
	 * 根据参数组ID集合查询属性
	 *
	 * @param groupIds 参数组ID集合
	 * @return 属性列表
	 */
	List<ProductPropertyBO> queryByGroupId(List<Long> groupIds);


	Long createId();

	/**
	 * 批量保存产品属性信息
	 *
	 * @param propertyList 待保存的产品属性对象列表
	 */
	void save(List<ProductPropertyDTO> propertyList);

	/**
	 * 根据ID集合查询详细信息
	 *
	 * @param id ID集合
	 * @return 包含详细信息的产品属性业务对象列表
	 */
	List<ProductPropertyBO> getDetailByIds(List<Long> id);

	/**
	 * 根据图片路径查找图片
	 *
	 * @param url 图片路径
	 * @return 图片路径列表
	 */
	List<String> queryAttachmentByUrl(String url);

}
