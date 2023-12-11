/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import com.legendshop.basic.dao.SystemConfigDao;
import com.legendshop.basic.dto.SystemConfigDTO;
import com.legendshop.basic.entity.SystemConfig;
import com.legendshop.basic.service.SystemConfigService;
import com.legendshop.basic.service.convert.SystemConfigConverter;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.common.core.util.SqlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统配置
 *
 * @author legendshop
 */
@Service
public class SystemConfigServiceImpl extends BaseServiceImpl<SystemConfigDTO, SystemConfigDao, SystemConfigConverter> implements SystemConfigService {

	@Autowired
	private SystemConfigDao systemConfigDao;

	@Autowired
	private SystemConfigConverter converter;

	/**
	 * 基本信息查询
	 *
	 * @return
	 */
	@Override
	public SystemConfigDTO getSystemConfig() {
		return converter.to(systemConfigDao.getSystemConfig());
	}

	@Override
	public Boolean update(SystemConfigDTO dto) {
		SystemConfig systemConfig = systemConfigDao.getSystemConfig();
		if (null == systemConfig) {
			super.save(dto);
			return true;
		}
		dto.setId(systemConfig.getId());
		return SqlUtil.retBool(systemConfigDao.update(converter.from(dto)));
	}
}
