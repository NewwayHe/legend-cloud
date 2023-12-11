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
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.dto.ProductPropertyDTO;
import com.legendshop.product.entity.ProductPropertyValue;

import java.util.List;

/**
 * 商品属性值服务
 *
 * @author legendshop
 */
public interface ProductPropertyValueDao extends GenericDao<ProductPropertyValue, Long> {

	List<ProductPropertyValue> getAllProductPropertyValue(List<ProductPropertyDTO> propertyList);

	List<ProductPropertyValue> getProductPropertyValue(List<Long> ids);

	/**
	 * 根据propId获取属性
	 *
	 * @param propId
	 * @return
	 */
	List<Long> getValueIdsByPropId(Long propId);

	/**
	 * 根据propId获取属性
	 *
	 * @param propertyId
	 * @return
	 */
	List<ProductPropertyValue> getByPropId(long propertyId);

	/**
	 * 根据属性 ID 和名称获取属性值。
	 *
	 * @param propertyId 属性 ID
	 * @param name       属性值名称
	 * @return 匹配属性 ID 和名称的属性值对象
	 */
	ProductPropertyValue getByPropId(long propertyId, String name);

	/**
	 * 更新属性值的删除标志。
	 *
	 * @param valueId    属性值 ID
	 * @param deleteFlag 删除标志，true 表示已删除，false 表示未删除
	 */
	void updateDeleteFlag(Long valueId, Boolean deleteFlag);

	/**
	 * 根据属性 ID 更新属性值的删除标志。
	 *
	 * @param id          属性 ID
	 * @param deleteFlag  删除标志，true 表示已删除，false 表示未删除
	 */
	void updateDeleteFlagByPropId(Long id, boolean deleteFlag);

	/**
	 * 根据属性 ID 列表查询属性值列表。
	 *
	 * @param productIdList 属性 ID 列表
	 * @return 包含属性值的列表
	 */
	List<ProductPropertyValue> queryByPropId(List<Long> productIdList);

	/**
	 * 根据参数组 ID 查询参数组列表。
	 *
	 * @param id 参数组 ID
	 * @return 包含参数组信息的列表
	 */
	List<ProductPropertyBO> queryByGroupId(Long id);

	/**
	 * 根据参数组 ID 列表查询参数组列表。
	 *
	 * @param groupIds 参数组 ID 列表
	 * @return 包含参数组信息的列表
	 */
	List<ProductPropertyBO> queryByGroupId(List<Long> groupIds);

}
