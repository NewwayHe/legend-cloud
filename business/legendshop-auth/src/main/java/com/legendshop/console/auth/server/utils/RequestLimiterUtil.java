/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.console.auth.server.utils;

import com.legendshop.common.core.constant.R;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * token请求限制工具
 *
 * @author legendshop
 */
@Component
public class RequestLimiterUtil {

	private final static Integer MAX_COUNT = 20;

	private final static Long WAIT = 60 * 5L;

	private final RedisTemplate redisTemplate;

	public RequestLimiterUtil(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public R<Integer> limiter(String principal) {
		Integer count = (Integer) this.redisTemplate.opsForValue().get("LOGIN_PRINCIPAL_" + principal);

		if (count == null) {
			return R.ok(count);
		}

		if (count <= MAX_COUNT) {
			return R.ok(count);
		}

		return R.fail(count);
	}

	public void success(String principal) {
		this.redisTemplate.delete("LOGIN_PRINCIPAL_" + principal);
	}

	public void fail(String principal) {
		Integer count = (Integer) this.redisTemplate.opsForValue().get("LOGIN_PRINCIPAL_" + principal);
		if (count == null) {
			count = 0;
		}
		this.redisTemplate.opsForValue().set("LOGIN_PRINCIPAL_" + principal, ++count, WAIT, TimeUnit.SECONDS);
	}

}
