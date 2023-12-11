/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import com.legendshop.product.dto.BrandDTO;

import java.util.List;

/**
 * 类目关联跟品牌的关系服务
 *
 * @author legendshop
 */
public interface ProductPropertyAggBrandService {

	/**
	 * 根据关联品牌 ID 删除关联信息的方法。
	 *
	 * @param id 要删除关联品牌的 ID
	 * @return 删除操作影响的行数
	 */
	int deleteById(Long id);

	/**
	 * 根据类目 ID 查找关联的品牌列表的方法。
	 *
	 * @param id 类目 ID
	 * @return 包含关联品牌信息的品牌数据传输对象列表
	 */
	List<BrandDTO> queryBrandListByCategory(Long id);
}
