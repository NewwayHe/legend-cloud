/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import com.legendshop.product.dao.PriceLogDao;
import com.legendshop.product.dto.PriceLogDTO;
import com.legendshop.product.service.PriceLogService;
import com.legendshop.product.service.convert.PriceLogConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author legendshop
 */
@Service
public class PriceLogServiceImpl implements PriceLogService {

	@Autowired
	private PriceLogDao priceLogDao;

	@Autowired
	private PriceLogConverter priceLogConverter;

	@Override
	public Long savePriceLog(List<PriceLogDTO> Log) {
		return Long.valueOf(priceLogDao.save(priceLogConverter.from(Log)).size());
	}
}
