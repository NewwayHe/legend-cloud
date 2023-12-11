/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;


import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.dto.StockLogDTO;
import com.legendshop.product.query.StockLogQuery;

import java.util.List;

/**
 * 库存历史服务
 *
 * @author legendshop
 */
public interface StockLogService {

	Long saveStockLog(List<StockLogDTO> stockLog);

	PageSupport<StockLogDTO> loadStockLog(StockLogQuery stockLogQuery);
}
