/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.controller.user;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.UserTokenUtil;
import com.legendshop.user.dto.BaseUserQuery;
import com.legendshop.user.dto.VerifyUserDTO;
import com.legendshop.user.service.BaseUserService;
import com.legendshop.user.service.BasicUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.legendshop.basic.enums.SmsTemplateTypeEnum.*;

/**
 * @author legendshop
 */
@Slf4j
@RestController
@Tag(name = "短信验证码")
@RequiredArgsConstructor
@RequestMapping(value = "/basic/user")
public class BasicUserController {

	final HttpServletRequest request;

	final UserTokenUtil userTokenUtil;

	final BasicUserService userService;

	final BaseUserService baseUserService;

	@PostMapping(value = "/verifyCodeExchangeCertificate", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "【公共】修改:支付、登录、换绑、验证码获取临时凭证,需要手机号、验证码", tags = "支付、登录、换绑、验证码获取临时凭证")
	public R<String> verifyCodeExchangeCertificate(@RequestBody VerifyUserDTO userDTO) {
		if (MODIFY_BINDING_MOBILE.equals(userDTO.getCodeType()) || MODIFY_LOGIN_PASSWORD.equals(userDTO.getCodeType()) || MODIFY_PAY_PASSWORD.equals(userDTO.getCodeType())) {
			Long userId = userTokenUtil.getUserId(request);
			if (null == userId) {
				return R.fail("请重新登录！");
			}

			String userType = userTokenUtil.getUserType(request);
			if (null == userType) {
				return R.fail("请重新登录！");
			}

			String mobile = baseUserService.getMobile(new BaseUserQuery(userId, userType));
			userDTO.setMobile(mobile);
		}
		return this.userService.verifyCodeExchangeCertificate(userDTO);
	}
}
