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
import com.legendshop.order.entity.OrderUserAddress;

/**
 * 用户配送地址
 *
 * @author legendshop
 */
public interface OrderUserAddressDao extends GenericDao<OrderUserAddress, Long> {

	OrderUserAddress getByOrderId(Long addressOrderId);
}
