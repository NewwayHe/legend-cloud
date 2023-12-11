/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import com.legendshop.basic.dto.SystemConfigDTO;
import com.legendshop.common.core.service.BaseService;

/**
 * 系统配置
 *
 * @author legendshop
 */
public interface SystemConfigService extends BaseService<SystemConfigDTO> {

	/**
	 * 基本信息查询
	 *
	 * @return
	 */
	SystemConfigDTO getSystemConfig();


}
