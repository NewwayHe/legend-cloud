/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import com.legendshop.basic.dao.BusinessSettingDao;
import com.legendshop.basic.service.BusinessSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author legendshop
 */
@Service
public class BusinessSettingServiceImpl implements BusinessSettingService {

	@Autowired
	private BusinessSettingDao constTableDao;

	@Override
	public String getByType(String type) {
		return constTableDao.getByType(type);
	}

	@Override
	public boolean updateByType(String type, String value) {
		return constTableDao.updateByType(type, value);
	}

}
