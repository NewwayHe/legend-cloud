/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

/**
 * 类目关联跟类目的关系服务
 *
 * @author legendshop
 */
public interface ProductPropertyAggCategoryService {

	/**
	 * 根据ID删除关联类目的方法。
	 *
	 * @param id 关联类目ID
	 * @return 删除操作结果的整数值
	 */
	int deleteById(Long id);

	/**
	 * 根据聚合ID和类目ID保存
	 *
	 * @param aggId      聚合ID
	 * @param categoryId 类目ID
	 */
	void save(Long aggId, Long categoryId);

	/**
	 * 根据类目ID删除
	 *
	 * @param id 类目ID
	 */
	void deleteByCategoryId(Long id);
}
