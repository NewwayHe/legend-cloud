/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.legendshop.activity.dao.ShippingActiveDao;
import com.legendshop.activity.dto.ShippingActiveDTO;
import com.legendshop.activity.service.ShippingActiveService;
import com.legendshop.activity.service.convert.ShippingActiveConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 包邮活动Service
 *
 * @author legendshop
 */
@Service
public class ShippingActiveServiceImpl implements ShippingActiveService {

	@Autowired
	private ShippingActiveDao shippingActiveDao;

	@Autowired
	private ShippingActiveConverter converter;

	@Override
	public ShippingActiveDTO getShippingActive(Long id) {
		return converter.to(shippingActiveDao.getById(id));
	}


	@Override
	public Long saveShippingActive(ShippingActiveDTO shippingActiveDTO) {
		if (ObjectUtil.isNotNull(shippingActiveDTO.getId())) {
			updateShippingActive(shippingActiveDTO);
			return shippingActiveDTO.getId();
		}
		return shippingActiveDao.saveShippingActive(converter.from(shippingActiveDTO));
	}

	@Override
	public int updateShippingActive(ShippingActiveDTO shippingActiveDTO) {
		return shippingActiveDao.update(converter.from(shippingActiveDTO));
	}
}
