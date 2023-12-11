/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import com.legendshop.basic.dao.BusinessSettingDao;
import com.legendshop.basic.entity.BusinessSetting;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * @author legendshop
 */
@Repository
public class BusinessSettingDaoImpl extends GenericDaoImpl<BusinessSetting, Long> implements BusinessSettingDao {


	private final static String GET_BUSINESS_SETTING_SQL_STRING = "select value from ls_business_setting where type=?";
	private final static String UPDATE_BUSINESS_SETTING_SQL_STRING = "update ls_business_setting set value=? where type=?";


	@Override
	@Cacheable(value = "ConstTable", key = "#type")
	public String getByType(String type) {
		return get(GET_BUSINESS_SETTING_SQL_STRING, String.class, type);
	}


	@Override
	@CacheEvict(value = "ConstTable", key = "#type")
	public boolean updateByType(String type, String value) {
		return update(UPDATE_BUSINESS_SETTING_SQL_STRING, value, type) > 0;
	}

}
