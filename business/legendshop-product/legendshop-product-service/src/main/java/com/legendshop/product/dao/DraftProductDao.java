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
import com.legendshop.product.entity.DraftProduct;
import com.legendshop.product.enums.DraftProductStatusEnum;

import java.util.List;

/**
 * 商品SPU草稿表(DraftProduct)表数据库访问层
 *
 * @author legendshop
 * @since 2022-05-08 09:37:10
 */
public interface DraftProductDao extends GenericDao<DraftProduct, Long> {

	/**
	 * 根据商品Id获取草稿
	 *
	 * @param productId
	 * @return
	 */
	DraftProduct getByProductId(Long productId);

	/**
	 * 根据商品ID列表获取草稿
	 *
	 * @param productId
	 * @return
	 */
	List<DraftProduct> queryByProductId(List<Long> productId);

	/**
	 * 更新草稿状态
	 *
	 * @param productId
	 * @param oldStatus
	 * @param newStatus
	 */
	Integer updateStatus(Long productId, Integer oldStatus, DraftProductStatusEnum newStatus);

	/**
	 * 根据商品Id删除草稿
	 *
	 * @param productIds
	 * @return
	 */
	Integer deleteByProductId(List<Long> productIds);

	/**
	 * 根据商品Id删除草稿
	 *
	 * @param productId
	 * @return
	 */
	Integer deleteByProductId(Long productId);
}
