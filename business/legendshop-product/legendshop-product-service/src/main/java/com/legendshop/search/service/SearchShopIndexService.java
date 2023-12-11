/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.service;

/**
 * @author legendshop
 */
public interface SearchShopIndexService {

	/**
	 * 初始化所有店铺索引
	 *
	 * @return
	 */
	String initAllShopIndex();

	/**
	 * 删除所有店铺索引
	 *
	 * @return
	 */
	Boolean delAllShopIndex();

	/**
	 * 根据店铺Id重建索引
	 *
	 * @return
	 */
	String initByShopIdToShopIndex(Long shopId);

	/**
	 * 根据店铺id删除索引
	 *
	 * @return
	 */
	String delByShopIdToShopIdIndex(Long shopId);

}
