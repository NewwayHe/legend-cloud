/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.console.auth.server.endpoint;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.user.api.SocialLoginApi;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 社交登录认证跳转
 *
 * @author legendshop
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SocialAuthEndpoint {

	final SocialLoginApi socialLoginApi;

	@GetMapping(value = "/{type}")
	public void auth(@PathVariable String type, HttpServletResponse response) throws IOException {
		log.info("社交登录类型 : {}", type);

		R<String> url = this.socialLoginApi.authUrl(type);
		if (!url.getSuccess()) {
			throw new BusinessException("错误的认证类型！");
		}
		response.sendRedirect(url.getData());
	}
}
