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
 * 类目关联跟参数组的关系服务
 *
 * @author legendshop
 */
public interface ProductPropertyAggParamGroupService {
	/**
	 * 保存关联参数组的方法。
	 *
	 * @param aggDTO 包含要保存的关联参数组信息的数据传输对象
	 * @return 是否成功保存
	 */
	boolean save(ProductPropertyAggBatchDTO aggDTO);

	/**
	 * 删除关联参数组的方法。
	 *
	 * @param id 要删除的关联参数组的 ID
	 * @return 删除操作影响的行数
	 */
	int deleteById(Long id);
}
