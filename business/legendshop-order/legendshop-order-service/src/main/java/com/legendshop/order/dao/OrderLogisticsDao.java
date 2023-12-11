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
import com.legendshop.order.entity.OrderLogistics;

import java.util.List;

/**
 * 订单物流DAO
 *
 * @author legendshop
 */
public interface OrderLogisticsDao extends GenericDao<OrderLogistics, Long> {

	/**
	 * 根据订单ID查询订单物流信息
	 *
	 * @param orderId
	 * @return
	 */
	OrderLogistics getByOrderId(Long orderId);

	/**
	 * 根据订单获取物流
	 *
	 * @param orderIds
	 * @return
	 */
	List<OrderLogistics> queryByOrderIds(List<Long> orderIds);
}
