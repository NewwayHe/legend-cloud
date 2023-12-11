/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.activity.bo.FullActiveProductBO;
import com.legendshop.activity.entity.ShippingProduct;

import java.util.List;

/**
 * 包邮活动商品Dao
 *
 * @author legendshop
 */
public interface ShippingProductDao extends GenericDao<ShippingProduct, Long> {

	/**
	 * 根据活动ID和店铺ID查询满减活动下的商品列表。
	 *
	 * @param activeId 活动ID
	 * @param shopId   店铺ID
	 * @return 满减活动商品列表
	 */
	List<FullActiveProductBO> queryByActiveIdByShopId(Long activeId, Long shopId);

	/**
	 * 根据包邮ID和店铺ID查询包邮商品列表。
	 *
	 * @param activeId 包邮ID
	 * @param shopId   店铺ID
	 * @return 包邮商品列表
	 */
	List<ShippingProduct> queryByShippingIdAndShopId(Long activeId, Long shopId);

	/**
	 * 根据包邮ID和店铺ID删除包邮商品。
	 *
	 * @param id     包邮ID
	 * @param shopId 店铺ID
	 */
	void deleteByIdAndShopId(Long id, Long shopId);


}
