/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service;


import com.legendshop.order.dto.OrderUserAddressDTO;

/**
 * 订单地址服务
 *
 * @author legendshop
 */
public interface OrderUserAddressService {

	OrderUserAddressDTO getById(Long id);

	void deleteById(Long id);

	Long save(OrderUserAddressDTO orderUserAddressDTO);

	void update(OrderUserAddressDTO orderUserAddressDTO);
}
