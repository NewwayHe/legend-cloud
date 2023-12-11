/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 分销商店信息
 *
 * @author legendshop
 */
@FeignClient(contextId = "PubApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface PubApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	/**
	 * 获取用户未读的公告数
	 *
	 * @param userId
	 * @return
	 */
	@GetMapping(value = PREFIX + "/pub/userUnreadMsg")
	R<Integer> userUnreadMsg(@RequestParam("userId") Long userId);
}
