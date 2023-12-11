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
import com.legendshop.product.entity.DraftSku;

import java.util.List;

/**
 * 单品SKU草稿表(DraftSku)表数据库访问层
 *
 * @author legendshop
 * @since 2022-05-08 09:37:05
 */
public interface DraftSkuDao extends GenericDao<DraftSku, Long> {

	/**
	 * 根据商品ID获取
	 *
	 * @param productId
	 * @return
	 */
	List<DraftSku> getSkuByProductId(Long productId);

	/**
	 * 根据商品Id删除sku
	 *
	 * @param productId
	 * @return
	 */
	Integer deleteByProductId(Long productId);

	/**
	 * 根据商品Id删除sku
	 *
	 * @param productIds
	 * @return
	 */
	Integer deleteByProductId(List<Long> productIds);
}
