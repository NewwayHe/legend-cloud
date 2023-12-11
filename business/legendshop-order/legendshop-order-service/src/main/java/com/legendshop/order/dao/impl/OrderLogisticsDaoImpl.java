/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.order.dao.OrderLogisticsDao;
import com.legendshop.order.entity.OrderLogistics;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * 订单服务DAO实现
 *
 * @author legendshop
 */
@Repository
public class OrderLogisticsDaoImpl extends GenericDaoImpl<OrderLogistics, Long> implements OrderLogisticsDao {


	@Override
	public OrderLogistics getByOrderId(Long orderId) {
		List<OrderLogistics> logistics = queryByProperties(new EntityCriterion().eq("orderId", orderId));
		if (CollUtil.isEmpty(logistics)) {
			return null;
		}
		return logistics.get(0);
	}

	@Override
	public List<OrderLogistics> queryByOrderIds(List<Long> orderIds) {
		if (CollUtil.isEmpty(orderIds)) {
			return Collections.emptyList();
		}

		LambdaEntityCriterion<OrderLogistics> criterion = new LambdaEntityCriterion<>(OrderLogistics.class);
		criterion.in(OrderLogistics::getOrderId, orderIds);
		return queryByProperties(criterion);
	}
}
