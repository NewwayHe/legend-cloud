/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;


import com.legendshop.common.core.service.BaseService;
import com.legendshop.product.dto.ProductGroupRelationDTO;

import java.util.List;

/**
 * 商品-分组关联服务接口
 *
 * @author legendshop
 */
public interface ProductGroupRelationService extends BaseService<ProductGroupRelationDTO> {

	/**
	 * 根据分组ID获取商品ID集合
	 *
	 * @param prodGroupId
	 * @return
	 */
	List<Long> getProductIdListByGroupId(Long prodGroupId);

	/**
	 * 分组关联商品
	 *
	 * @param productIds
	 * @param groupId
	 * @return
	 */
	boolean saveRelation(String productIds, Long groupId);

	/**
	 * 商品关联分组
	 *
	 * @param productIds
	 * @param groupIds
	 * @return
	 */
	boolean saveRelation(String productIds, String groupIds);
}
