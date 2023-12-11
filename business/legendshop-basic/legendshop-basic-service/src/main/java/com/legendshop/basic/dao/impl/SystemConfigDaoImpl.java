/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;


import cn.hutool.core.collection.CollUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import com.legendshop.basic.dao.SystemConfigDao;
import com.legendshop.basic.entity.SystemConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统配置Dao实现类
 *
 * @author legendshop
 */
@Repository("systemConfigDao")
public class SystemConfigDaoImpl extends GenericDaoImpl<SystemConfig, Long> implements SystemConfigDao {

	/**
	 * 系统配置只有一条记录
	 */
	@Override
	public SystemConfig getSystemConfig() {
		List<SystemConfig> systemConfigs = queryAll();
		if (CollUtil.isEmpty(systemConfigs)) {
			return null;
		}
		return systemConfigs.get(0);
	}

}

