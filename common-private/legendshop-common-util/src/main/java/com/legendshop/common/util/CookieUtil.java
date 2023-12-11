/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.util;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.WebUtils;

/**
 * @author legendshop
 */
public class CookieUtil {

	/**
	 * 默认cookie有效时间为30天
	 */
	private static final Integer MAX_AGE = 2592000;

	/**
	 * 缓存key
	 */
	private static final String USER_KET = "user_key";

	/**
	 * 构建redis的缓存key
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	public static String buildCookieKey(HttpServletRequest request, HttpServletResponse response) {
		//最终返回的key
		String cookieKey;
		Cookie cartCookie = WebUtils.getCookie(request, USER_KET);
		if (ObjectUtil.isNull(cartCookie)) {
			cookieKey = UUID.randomUUID().toString();
			Cookie cookie = new Cookie(USER_KET, cookieKey);
			cookie.setMaxAge(MAX_AGE);
			cookie.setPath("/");
			response.addCookie(cookie);
		} else {
			//获取Cookie中的key
			cookieKey = cartCookie.getValue();
		}
		return cookieKey;
	}
}
