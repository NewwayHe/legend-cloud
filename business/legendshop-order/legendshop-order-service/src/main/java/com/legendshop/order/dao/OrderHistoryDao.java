/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.order.entity.OrderHistory;

import java.util.List;

/**
 * 订单历史
 *
 * @author legendshop
 */
public interface OrderHistoryDao extends GenericDao<OrderHistory, Long> {

	/**
	 * 根据订单号查询订单历史
	 *
	 * @param orderId
	 * @return
	 */
	List<OrderHistory> queryByOrderId(Long orderId);

	/**
	 * 保存订单历史.
	 *
	 * @param orderId         the orderId
	 * @param operationStatus the operationStatus
	 */
	void saveByOrderId(Long orderId, String operationStatus);
}
