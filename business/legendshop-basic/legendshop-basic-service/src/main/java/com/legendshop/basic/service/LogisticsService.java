/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import com.legendshop.common.logistics.kuaidi100.request.SubscribeReq;

/**
 * @author legendshop
 */
public interface LogisticsService {

	boolean poll(SubscribeReq subscribeReq);

}
