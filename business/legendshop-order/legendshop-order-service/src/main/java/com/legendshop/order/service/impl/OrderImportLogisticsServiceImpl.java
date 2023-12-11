/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.impl;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.order.dao.OrderDao;
import com.legendshop.order.dao.OrderImportLogisticsDao;
import com.legendshop.order.dto.OrderImportLogisticsDTO;
import com.legendshop.order.dto.WaitDeliveryOrderDTO;
import com.legendshop.order.query.OrderImportLogisticsQuery;
import com.legendshop.order.service.OrderImportLogisticsService;
import com.legendshop.order.service.convert.OrderImportLogisticsConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * (OrderImportLogistics)表服务实现类
 *
 * @author legendshop
 * @since 2022-04-25 14:13:35
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderImportLogisticsServiceImpl implements OrderImportLogisticsService {

	private final OrderImportLogisticsDao orderImportLogisticsDao;
	private final OrderDao orderDao;
	private final OrderImportLogisticsConverter converter;


	@Override
	public PageSupport<OrderImportLogisticsDTO> page(OrderImportLogisticsQuery query) {
		return orderImportLogisticsDao.page(query);
	}

	@Override
	public List<WaitDeliveryOrderDTO> template(Long shopId) {
		//查询待发货没有售后的订单
		List<WaitDeliveryOrderDTO> orderList = orderDao.queryByStatusAddress(shopId);
		if (CollectionUtils.isEmpty(orderList)) {
			return Collections.emptyList();
		}
		return orderList;
	}

	@Override
	public Long save(OrderImportLogisticsDTO importLogisticsDTO) {
		return this.orderImportLogisticsDao.save(this.converter.from(importLogisticsDTO));
	}

}
