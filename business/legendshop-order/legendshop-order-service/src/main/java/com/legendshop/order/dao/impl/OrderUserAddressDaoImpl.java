/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.order.dao.OrderUserAddressDao;
import com.legendshop.order.entity.OrderUserAddress;
import org.springframework.stereotype.Repository;

/**
 * 用户配送地址Dao
 *
 * @author legendshop
 */
@Repository
public class OrderUserAddressDaoImpl extends GenericDaoImpl<OrderUserAddress, Long> implements OrderUserAddressDao {

	@Override
	public OrderUserAddress getByOrderId(Long addressOrderId) {

		LambdaEntityCriterion<OrderUserAddress> criterion = new LambdaEntityCriterion<>(OrderUserAddress.class);
		criterion.eq(OrderUserAddress::getId, addressOrderId);
		return getByProperties(criterion);
	}
}
