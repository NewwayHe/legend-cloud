/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.dao.StockLogDao;
import com.legendshop.product.dto.StockLogDTO;
import com.legendshop.product.query.StockLogQuery;
import com.legendshop.product.service.StockLogService;
import com.legendshop.product.service.convert.StockLogConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 库存历史服务
 *
 * @author legendshop
 */
@Service
public class StockLogServiceImpl implements StockLogService {

	@Autowired
	private StockLogDao stockLogDao;

	@Autowired
	private StockLogConverter stockLogConverter;

	@Override
	public Long saveStockLog(List<StockLogDTO> stockLog) {
		return Long.valueOf(stockLogDao.save(stockLogConverter.from(stockLog)).size());
	}

	@Override
	public PageSupport<StockLogDTO> loadStockLog(StockLogQuery stockLogQuery) {
		return stockLogConverter.page(stockLogDao.loadStockLog(stockLogQuery));
	}
}
