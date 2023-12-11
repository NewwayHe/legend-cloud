/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.console.auth.server.endpoint;

import cn.hutool.core.util.StrUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.RequestHeaderConstant;
import com.legendshop.common.core.enums.UserTypeEnum;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.console.auth.server.service.TokenHandleService;
import com.legendshop.user.api.PassportApi;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

/**
 * 删除token入口
 *
 * @author legendshop
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class RevokeTokenEndpoint {

	private final PassportApi passportApi;
	private final TokenHandleService tokenHandleService;


	/**
	 * 退出token
	 *
	 * @param authHeader Authorization
	 */
	@DeleteMapping("/logout")
	public R<Boolean> logout(HttpServletRequest request, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader, @RequestParam String userType) {
		if (StrUtil.isBlank(authHeader)) {
			return R.ok(Boolean.FALSE, "退出失败，token为空");
		}
		if (!UserTypeEnum.contains(userType)) {
			return R.ok(Boolean.FALSE, "退出失败，类型为空");
		}

		String source = request.getHeader(RequestHeaderConstant.SOURCE_KEY);
		String tokenValue = authHeader.replace(OAuth2AccessToken.TokenType.BEARER.getValue(), StrUtil.EMPTY).trim();
		Long userId = null;
		if (userId != null && userId != 0) {
			String type;
			if (VisitSourceEnum.MP.name().equals(source) || VisitSourceEnum.MINI.name().equals(source) || VisitSourceEnum.APP.name().equals(source)) {
				type = "WE_CHAT";
				passportApi.unboundUserPassport(userId, type, source);
			}
		}
		return delToken(tokenValue, userType);
	}

	/**
	 * 令牌管理调用
	 *
	 * @param token token
	 * @return R
	 */
	@DeleteMapping("/{token}")
	public R<Boolean> delToken(@PathVariable("token") String token, @RequestParam String userType) {
		try {
			tokenHandleService.delToken(token, userType);
			return R.ok();
		} catch (Exception ex) {
			return R.fail(ex.getMessage());
		}
	}

	/**
	 * 安全退出登录
	 *
	 * @param clientId clientId
	 * @param username 用户名
	 * @return
	 */
	@DeleteMapping("/safe/logout")
	public R<Boolean> safeLogout(@RequestParam String clientId, @RequestParam String username) {
		return R.ok(Boolean.TRUE);
	}

}
