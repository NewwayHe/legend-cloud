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
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.entity.StockLog;
import com.legendshop.product.query.StockLogQuery;

/**
 * 库存历史服务Dao
 *
 * @author legendshop
 */
public interface StockLogDao extends GenericDao<StockLog, Long> {

	PageSupport<StockLog> loadStockLog(StockLogQuery stockLogQuery);

}
