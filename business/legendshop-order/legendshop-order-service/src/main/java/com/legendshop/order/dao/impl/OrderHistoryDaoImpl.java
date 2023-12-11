/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dao.impl;

import cn.hutool.core.date.DateUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import com.legendshop.order.dao.OrderHistoryDao;
import com.legendshop.order.entity.OrderHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 保存订单历史dao实现
 *
 * @author legendshop
 */
@Repository
public class OrderHistoryDaoImpl extends GenericDaoImpl<OrderHistory, Long> implements OrderHistoryDao {


	@Override
	public List<OrderHistory> queryByOrderId(Long orderId) {
		return queryByProperties(new EntityCriterion().eq("orderId", orderId).addAscOrder("recDate"));
	}

	@Override
	public void saveByOrderId(Long orderId, String operationStatus) {
		OrderHistory history = new OrderHistory();
		history.setCreateTime(DateUtil.date());
		history.setStatus(operationStatus);
		history.setOrderId(orderId);
		save(history);
	}

}
