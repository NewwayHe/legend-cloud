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
import com.legendshop.user.dto.BasisLoginParamsDTO;
import com.legendshop.user.dto.UserInfo;
import com.legendshop.user.service.SocialLoginHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Feign调用实现
 *
 * @author legendshop
 */
@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
public class SocialLoginApiImpl implements SocialLoginApi {

	final Map<String, SocialLoginHandler> loginHandlerMap;

	@Override
	public R<UserInfo> handle(BasisLoginParamsDTO loginParams) {
		log.info("进行第三方登录，参数：{}", loginParams);
		return this.loginHandlerMap.get(loginParams.getAuthType().authType()).handle(loginParams);
	}

	@Override
	public R<String> authUrl(String authType) {
		String authUrl = this.loginHandlerMap.get(authType).authUrl();
		log.info("跳转第三方社交登录，参数,{}", authUrl);
		return R.ok(authUrl);
	}

	@Override
	public R<Boolean> updateUserOpenId(BasisLoginParamsDTO loginParams) {
		return this.loginHandlerMap.get(loginParams.getAuthType().authType()).updateUserOpenId(loginParams);
	}
}
