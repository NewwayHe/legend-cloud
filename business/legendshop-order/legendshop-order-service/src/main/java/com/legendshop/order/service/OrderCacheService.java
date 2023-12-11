/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service;

import com.legendshop.order.bo.ConfirmOrderBO;

/**
 * 订单缓存处理
 *
 * @author legendshop
 */
public interface OrderCacheService {

	/**
	 * 将预订单信息放入缓存
	 *
	 * @param userId         用户ID
	 * @param confirmOrderBO
	 * @return
	 */
	ConfirmOrderBO putConfirmOrderInfoCache(Long userId, ConfirmOrderBO confirmOrderBO);


	/**
	 * 获取预订单信息缓存
	 *
	 * @param userId         用户ID
	 * @param confirmOrderId 预订单ID
	 * @return
	 */
	ConfirmOrderBO getConfirmOrderInfoCache(Long userId, String confirmOrderId);

	/**
	 * 移除预订单信息缓存
	 *
	 * @param userId         用户ID
	 * @param confirmOrderId 预订单ID
	 */
	void evictConfirmOrderInfoCache(Long userId, String confirmOrderId);
}
