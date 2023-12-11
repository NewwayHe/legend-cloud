/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import com.legendshop.basic.service.SysParamCacheService;
import com.legendshop.common.core.constant.CacheConstants;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

/**
 * @author legendshop
 */
@Service
public class SysParamCacheServiceImpl implements SysParamCacheService {


	@Override
	@CacheEvict(value = CacheConstants.SYSTEM_PARAMS_DETAILS, key = "#name  + '_sys'", beforeInvocation = true)
	public void evictConfigDtoByParamName(String name) {

	}
}
