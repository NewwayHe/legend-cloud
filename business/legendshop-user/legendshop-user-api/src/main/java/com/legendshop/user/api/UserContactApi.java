/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.user.bo.UserContactBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author legendshop
 */
@FeignClient(contextId = "userContactApi", value = ServiceNameConstants.USER_SERVICE)
public interface UserContactApi {

	String PREFIX = ServiceNameConstants.USER_SERVICE_RPC_PREFIX;

	/**
	 * 获取用户的默认提货信息。
	 *
	 * @param userId 用户ID
	 * @return 用户默认提货信息
	 */
	@GetMapping(value = PREFIX + "/p/contact/getDefaultUserContact")
	R<UserContactBO> getDefaultUserContact(@RequestParam(value = "userId") Long userId);

	/**
	 * 根据提货信息ID获取提货信息详情。
	 *
	 * @param id 提货信息ID
	 * @return 提货信息详情
	 */
	@GetMapping(value = PREFIX + "/p/contact/getById")
	R<UserContactBO> getById(@RequestParam(value = "id") Long id);

	/**
	 * 获取用户用于订单的提货信息。
	 *
	 * @param userId    用户ID
	 * @param contactId 提货信息ID（可选）
	 * @return 用户用于订单的提货信息
	 */
	@GetMapping(value = PREFIX + "/p/contact/getUserContactForOrder")
	R<UserContactBO> getUserContactForOrder(@RequestParam(value = "userId") Long userId,
											@RequestParam(value = "contactId", required = false) Long contactId);

}
