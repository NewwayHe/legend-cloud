/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service;

import com.legendshop.order.dto.OrderHistoryDTO;

import java.util.List;

/**
 * 订单历史服务
 *
 * @author legendshop
 */
public interface OrderHistoryService {

	/**
	 * 保存订单历史
	 */
	Long save(OrderHistoryDTO orderHistoryDTO);

	List<Long> save(List<OrderHistoryDTO> orderHistoryList);

	/**
	 * 获取订单的操作操作历史
	 */
	String getOrderHistoryInfo(Long orderId);
}
