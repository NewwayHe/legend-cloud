/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.basic.entity.BusinessSetting;

/**
 * 业务配置DAO
 *
 * @author legendshop
 */
public interface BusinessSettingDao extends GenericDao<BusinessSetting, Long> {


	/**
	 * 根据类型查询配置值
	 *
	 * @param type
	 * @return
	 */
	String getByType(String type);

	/**
	 * 根据类型更新配置值
	 *
	 * @param type
	 * @param value
	 * @return
	 */
	boolean updateByType(String type, String value);


}
