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
import com.legendshop.product.entity.ProductGroupRelation;

import java.util.List;

/**
 * Dao接口
 *
 * @author legendshop
 */
public interface ProductGroupRelationDao extends GenericDao<ProductGroupRelation, Long> {


	/**
	 * 根据分组Id删除
	 *
	 * @param groupId
	 * @return
	 */
	int deleteProdGroupRelevanceByGroupId(Long groupId);

	/**
	 * 根据分组ID和商品ID判断商品是否已被关联
	 *
	 * @param productId
	 * @param groupId
	 * @return
	 */
	int getProductByGroupId(Long productId, Long groupId);

	/**
	 * 根据分组ID获取商品ID集合
	 *
	 * @param prodGroupId
	 * @return
	 */
	List<Long> getProductIdListByGroupId(Long prodGroupId);

}
