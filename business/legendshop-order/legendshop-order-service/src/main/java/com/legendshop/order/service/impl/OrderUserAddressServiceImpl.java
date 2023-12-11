/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.impl;

import com.legendshop.order.dao.OrderUserAddressDao;
import com.legendshop.order.dto.OrderUserAddressDTO;
import com.legendshop.order.service.OrderUserAddressService;
import com.legendshop.order.service.convert.OrderUserAddressConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户配送地址服务
 *
 * @author legendshop
 */
@Service
public class OrderUserAddressServiceImpl implements OrderUserAddressService {

	@Autowired
	private OrderUserAddressDao orderUserAddressDao;

	@Autowired
	private OrderUserAddressConverter orderUserAddressConverter;

	@Override
	public OrderUserAddressDTO getById(Long id) {
		return orderUserAddressConverter.to(orderUserAddressDao.getById(id));
	}

	@Override
	public void deleteById(Long id) {
		orderUserAddressDao.deleteById(id);
	}

	@Override
	public Long save(OrderUserAddressDTO userAddressOrder) {
		return orderUserAddressDao.save(orderUserAddressConverter.from(userAddressOrder));
	}

	@Override
	public void update(OrderUserAddressDTO userAddressOrder) {
		orderUserAddressDao.update(orderUserAddressConverter.from(userAddressOrder));
	}
}
