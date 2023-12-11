/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import com.legendshop.data.dao.ShopOutStockRateDao;
import com.legendshop.data.entity.ShopOutStockRate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (ShopOutStockRate)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2021-06-17 13:43:43
 */
@Repository
public class ShopOutStockRateDaoImpl extends GenericDaoImpl<ShopOutStockRate, Long> implements ShopOutStockRateDao {

	@Override
	public List<ShopOutStockRate> queryShopOutStockRate() {
		String sql = "SELECT lp.shop_id, COUNT(IF(ls.stocks <= lp.stocks_arm, 1, NULL)) / COUNT(*) AS out_stock_rate FROM ls_product lp LEFT JOIN ls_sku ls ON lp.id = ls.product_id where lp.status=1 and lp.op_status=1 and lp.del_status=1 GROUP BY lp.shop_id";
		return query(sql, ShopOutStockRate.class);
	}
}
