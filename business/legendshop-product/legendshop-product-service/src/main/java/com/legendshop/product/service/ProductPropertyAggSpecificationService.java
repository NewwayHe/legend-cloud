/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;


import com.legendshop.product.dto.ProductPropertyAggBatchDTO;

/**
 * 类目关联跟属性的关系服务
 *
 * @author legendshop
 */
public interface ProductPropertyAggSpecificationService {

	/**
	 * 批量保存
	 *
	 * @param aggDTO 包含要保存信息的数据传输对象
	 * @return 是否成功保存
	 */
	boolean save(ProductPropertyAggBatchDTO aggDTO);

	/**
	 * 根据属性 ID 删除相关数据的方法。
	 *
	 * @param productId 属性 ID
	 * @return 删除操作影响的行数
	 */
	int deleteByProductId(Long productId);

	/**
	 * 根据ID删除相关数据的方法。
	 *
	 * @param id 要删除的数据的ID
	 * @return 删除操作影响的行数
	 */
	int deleteById(Long id);

}
