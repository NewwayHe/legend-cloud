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
import com.legendshop.user.dto.BasisLoginParamsDTO;
import com.legendshop.user.dto.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author legendshop
 */
@FeignClient(contextId = "socialLoginApi", value = ServiceNameConstants.USER_SERVICE)
public interface SocialLoginApi {

	String PREFIX = ServiceNameConstants.USER_SERVICE_RPC_PREFIX;

	/**
	 * 处理登录方法
	 */
	@PostMapping(value = PREFIX + "/social/login/handle")
	R<UserInfo> handle(@RequestBody BasisLoginParamsDTO loginParams);

	/**
	 * 跳转登录地址
	 */
	@GetMapping(value = PREFIX + "/social/login/authUrl")
	R<String> authUrl(@RequestParam(value = "authType") String authType);

	/**
	 * 更新用户对应的OpenId
	 */
	@PostMapping(value = PREFIX + "/social/login/updateUserOpenId")
	R<Boolean> updateUserOpenId(@RequestBody BasisLoginParamsDTO loginParams);
}
