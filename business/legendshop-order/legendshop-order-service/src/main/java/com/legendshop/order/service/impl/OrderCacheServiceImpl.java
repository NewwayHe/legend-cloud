/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.impl;

import com.legendshop.order.bo.ConfirmOrderBO;
import com.legendshop.order.service.OrderCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author legendshop
 */
@Slf4j
@Service
public class OrderCacheServiceImpl implements OrderCacheService {

	@Override
	@CachePut(value = "ConfirmOrderBO", key = "'ConfirmOrderBO_' + #userId + '_' + #confirmOrderBO.id")
	public ConfirmOrderBO putConfirmOrderInfoCache(Long userId, ConfirmOrderBO confirmOrderBO) {
		return confirmOrderBO;
	}


	@Override
	@Cacheable(value = "ConfirmOrderBO", key = "'ConfirmOrderBO_' + #userId + '_' + #confirmOrderId")
	public ConfirmOrderBO getConfirmOrderInfoCache(Long userId, String confirmOrderId) {
		return null;
	}


	@CacheEvict(value = "ConfirmOrderBO", key = "'ConfirmOrderBO_'+#userId+'_'+#confirmOrderId")
	@Override
	public void evictConfirmOrderInfoCache(Long userId, String confirmOrderId) {

	}

}
