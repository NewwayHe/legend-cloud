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
import com.legendshop.basic.entity.SystemConfig;

/**
 * 系统配置Dao.
 *
 * @author legendshop
 */
public interface SystemConfigDao extends GenericDao<SystemConfig, Long> {

	/**
	 * 系统配置只有一条记录
	 */
	SystemConfig getSystemConfig();


}
