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
import com.legendshop.product.entity.ProductPropertyParamGroup;

import java.util.List;

/**
 * @author legendshop
 */
public interface ProductPropertyParamGroupDao extends GenericDao<ProductPropertyParamGroup, Long> {

	/**
	 * 根据参数组 ID 删除相关数据的方法。
	 *
	 * @param id 参数组 ID
	 * @return 删除操作影响的行数
	 */
	int deleteByGroupId(Long id);

	/**
	 * 根据参数组 ID 获取关联的个数。
	 *
	 * @param id 参数组 ID
	 * @return 关联数量
	 */
	int getCountByGroupId(Long id);

	/**
	 * 根据参数组 ID 集合查询参数属性名称的方法。
	 *
	 * @param groupIdList 参数组 ID 列表
	 * @return 包含参数属性名称的列表
	 */
	List<ProductPropertyBO> queryPropertyNameByGroupId(List<Long> groupIdList);


}
