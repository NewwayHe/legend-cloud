/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.dao.StockLogDao;
import com.legendshop.product.entity.StockLog;
import com.legendshop.product.query.StockLogQuery;
import org.springframework.stereotype.Repository;

/**
 * 库存历史服务Dao
 *
 * @author legendshop
 */
@Repository
public class StockLogDaoImpl extends GenericDaoImpl<StockLog, Long> implements StockLogDao {

	@Override
	public PageSupport<StockLog> loadStockLog(StockLogQuery stockLogQuery) {
		CriteriaQuery cq = new CriteriaQuery(StockLog.class, stockLogQuery.getPageSize(), stockLogQuery.getCurPage());
		cq.eq("productId", stockLogQuery.getProductId());
		cq.eq("skuId", stockLogQuery.getSkuId());
		cq.addDescOrder("updateTime");
		return queryPage(cq);
	}
}
