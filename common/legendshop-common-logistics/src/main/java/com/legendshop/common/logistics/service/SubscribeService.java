/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.service;

import com.legendshop.common.logistics.kuaidi100.request.SubscribeReq;
import com.legendshop.common.logistics.kuaidi100.response.SubscribeResp;

/**
 * 订阅的请求
 *
 * @author legendshop
 */
public interface SubscribeService {

	/**
	 * 订阅的请求
	 *
	 * @param subscribeReq
	 * @return
	 */
	SubscribeResp subscribe(SubscribeReq subscribeReq);
}
