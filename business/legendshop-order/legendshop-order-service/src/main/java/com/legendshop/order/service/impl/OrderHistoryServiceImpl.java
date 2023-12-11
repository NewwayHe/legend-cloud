/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.legendshop.common.core.dto.KeyValueEntityDTO;
import com.legendshop.order.dao.OrderHistoryDao;
import com.legendshop.order.dto.OrderHistoryDTO;
import com.legendshop.order.entity.OrderHistory;
import com.legendshop.order.service.OrderHistoryService;
import com.legendshop.order.service.convert.OrderHistoryConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单历史服务
 *
 * @author legendshop
 */
@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {


	@Autowired
	private OrderHistoryDao orderHistoryDao;

	@Autowired
	private OrderHistoryConverter orderHistoryConverter;

	/**
	 * 保存订单历史
	 */
	@Override
	public Long save(OrderHistoryDTO orderHistoryDTO) {
		return orderHistoryDao.save(orderHistoryConverter.from(orderHistoryDTO));
	}

	@Override
	public List<Long> save(List<OrderHistoryDTO> orderHistoryList) {
		return this.orderHistoryDao.save(this.orderHistoryConverter.from(orderHistoryList));
	}

	@Override
	public String getOrderHistoryInfo(Long orderId) {
		List<OrderHistory> histories = orderHistoryDao.queryByOrderId(orderId);
		String info = "";
		if (CollUtil.isNotEmpty(histories)) {
			List<KeyValueEntityDTO> entities = new ArrayList<KeyValueEntityDTO>();
			for (int i = 0; i < histories.size(); i++) {
				KeyValueEntityDTO keyValueListEntity = new KeyValueEntityDTO(histories.get(i).getId().toString(), histories.get(i).getReason());
				entities.add(keyValueListEntity);
			}
			return JSONUtil.toJsonStr(entities);
		}
		return info;
	}
}
