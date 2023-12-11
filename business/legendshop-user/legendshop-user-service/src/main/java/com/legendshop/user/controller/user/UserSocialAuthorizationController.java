/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.controller.user;

import cn.hutool.extra.servlet.JakartaServletUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.user.dto.BasisLoginParamsDTO;
import com.legendshop.user.service.SocialLoginHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户第三方授权
 *
 * @author legendshop
 * @create: 2021-11-19 09:41
 */
@Slf4j
@RestController
@Tag(name = "用户第三方授权")
@RequiredArgsConstructor
@RequestMapping(value = "/user/social/login")
public class UserSocialAuthorizationController {

	final HttpServletRequest request;
	final Map<String, SocialLoginHandler> loginHandlerMap;

	@PostMapping(value = "/updateUserOpenId")
	public R<Boolean> updateUserOpenId(@RequestBody BasisLoginParamsDTO loginParams, @RequestHeader(value = "source") String source) {
		loginParams.setSource(source);
		loginParams.setPrincipal(SecurityUtils.getUserName());
		loginParams.setIp(JakartaServletUtil.getClientIP(request));
		return this.loginHandlerMap.get(loginParams.getAuthType().authType()).updateUserOpenId(loginParams);
	}
}
