/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.impl;

import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.order.dao.OrderImportLogisticsDetailDao;
import com.legendshop.order.dto.OrderImportLogisticsDetailDTO;
import com.legendshop.order.excel.OrderImportLogisticsExportDTO;
import com.legendshop.order.service.OrderImportLogisticsDetailService;
import com.legendshop.order.service.convert.OrderImportLogisticsDetailConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (OrderImportLogisticsDetail)表服务实现类
 *
 * @author legendshop
 * @since 2022-04-25 14:12:17
 */
@Service
@RequiredArgsConstructor
public class OrderImportLogisticsDetailServiceImpl extends BaseServiceImpl<OrderImportLogisticsDetailDTO, OrderImportLogisticsDetailDao, OrderImportLogisticsDetailConverter> implements OrderImportLogisticsDetailService {

	private final OrderImportLogisticsDetailConverter converter;

	private final OrderImportLogisticsDetailDao orderImportLogisticsDetailDao;


	@Override
	public List<OrderImportLogisticsExportDTO> exportByImportId(Long importId) {
		return this.converter.toExport(this.orderImportLogisticsDetailDao.findByImportId(importId));
	}

}
