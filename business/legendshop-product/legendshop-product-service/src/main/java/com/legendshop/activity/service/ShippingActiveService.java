/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service;

import com.legendshop.activity.dto.ShippingActiveDTO;

/**
 * 包邮活动Service
 *
 * @author legendshop
 */
public interface ShippingActiveService {
	/**
	 * 根据Id获取包邮活动
	 */
	ShippingActiveDTO getShippingActive(Long id);

	/**
	 * 保存包邮活动
	 */
	Long saveShippingActive(ShippingActiveDTO shippingActiveDTO);

	/**
	 * 删除包邮活动
	 */
	int updateShippingActive(ShippingActiveDTO shippingActiveDTO);


}
