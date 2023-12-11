/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.order.fallback.CartApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author legendshop
 */
@FeignClient(contextId = "cartApi", name = ServiceNameConstants.ORDER_SERVICE, fallback = CartApiFallback.class)
public interface CartApi {

	String PREFIX = ServiceNameConstants.ORDER_SERVICE_RPC_PREFIX;

	/**
	 * 购物车合并
	 *
	 * @param userId  登录的用户
	 * @param userKey 未登录的userKey
	 * @return
	 */
	@PostMapping(PREFIX + "/cart/mergeCart")
	R<Void> mergeCart(@RequestParam("userId") Long userId, @RequestParam("userKey") String userKey);

}
