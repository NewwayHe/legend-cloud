/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 易宝支付接口
 *
 * @author legendshop
 * @create: 2021/3/29 10:56
 */
@FeignClient(contextId = "YeepayBusinessApi", value = ServiceNameConstants.ORDER_SERVICE)
public interface YeepayApi {

	String PREFIX = ServiceNameConstants.ORDER_SERVICE_RPC_PREFIX;

	/**
	 * 是否开启易宝支付
	 *
	 * @return
	 */
	@GetMapping(PREFIX + "/yeepay/enabled")
	R<Boolean> enabled();
}
