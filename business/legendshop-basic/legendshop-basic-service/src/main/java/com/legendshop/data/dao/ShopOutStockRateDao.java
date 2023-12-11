/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.data.entity.ShopOutStockRate;

import java.util.List;

/**
 * (ShopOutStockRate)表数据库访问层
 *
 * @author legendshop
 * @since 2021-06-17 13:43:42
 */
public interface ShopOutStockRateDao extends GenericDao<ShopOutStockRate, Long> {


	/**
	 * 查询店铺缺货率
	 */
	List<ShopOutStockRate> queryShopOutStockRate();

}
