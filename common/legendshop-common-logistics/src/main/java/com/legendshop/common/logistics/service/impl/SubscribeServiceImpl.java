/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.service.impl;

import com.legendshop.common.logistics.kuaidi100.api.Subscribe;
import com.legendshop.common.logistics.kuaidi100.request.SubscribeReq;
import com.legendshop.common.logistics.kuaidi100.response.SubscribeResp;
import com.legendshop.common.logistics.service.SubscribeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author legendshop
 */
@Service
@AllArgsConstructor
public class SubscribeServiceImpl implements SubscribeService {

	private final Subscribe subscribe;

	@Override
	public SubscribeResp subscribe(SubscribeReq subscribeReq) {
		return subscribe.subscribe(subscribeReq);
	}
}
