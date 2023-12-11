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
import com.legendshop.common.security.dto.BaseUserDetail;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.user.dto.BaseUserQuery;
import com.legendshop.user.dto.SmsVerificationCodeDTO;
import com.legendshop.user.service.BaseUserService;
import com.legendshop.user.service.SmsVerificationCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信验证码
 *
 * @author legendshop
 */
@Tag(name = "短信验证码")
@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserSendVerifyCodeController {

	final HttpServletRequest request;
	final BaseUserService baseUserService;
	final SmsVerificationCodeService smsVerificationCodeService;

	@PostMapping("/userSend/smsVerifyCode")
	@Operation(summary = "【公共】发送验证码，不需要登录")
	public R<String> sendSmsCode(@RequestBody SmsVerificationCodeDTO smsVerificationCode) {
		if (StringUtils.isBlank(smsVerificationCode.getMobile())) {
			return R.fail("请重新登录！");
		}
		smsVerificationCode.setIp(JakartaServletUtil.getClientIP(request));
		return this.smsVerificationCodeService.sendSmsCode(smsVerificationCode);
	}

	@PostMapping("/p/userSend/smsVerifyCode")
	@Operation(summary = "【公共】发送验证码，需要登录")
	public R<String> sendSmsCodeRequiredLogin(@RequestBody SmsVerificationCodeDTO smsVerificationCode) {
		BaseUserDetail baseUser = SecurityUtils.getBaseUser();
		if (null == baseUser) {
			return R.fail("请重新登录！");
		}
		String mobile = baseUserService.getMobile(new BaseUserQuery(baseUser.getUserId(), baseUser.getUserType()));
		smsVerificationCode.setMobile(mobile);
		smsVerificationCode.setIp(JakartaServletUtil.getClientIP(request));
		return this.smsVerificationCodeService.sendSmsCode(smsVerificationCode);
	}

}
