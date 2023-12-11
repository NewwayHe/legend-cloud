/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.data.cache.properties;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.temporal.ChronoUnit;

/**
 * @author legendshop
 */
@ConditionalOnExpression("!'${spring.redis.expired-time-type}'.isEmpty()")
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedisCacheProperties {

	/**
	 * 过期时间，默认为30
	 */
	private int expiredTime = 30;

	/**
	 * 单位,默认为分钟
	 */
	private ChronoUnit chronoUnit = ChronoUnit.MINUTES;

	/**
	 * 区间最小值，类型为Random，需要配置
	 */
	private int min = 30;

	/**
	 * 区间最大值，类型为Random，需要配置
	 */
	private int max = 60;


	/**
	 * 过期时间类型，默认为永久有效
	 */
	private ExpiredTimeType expiredTimeType = ExpiredTimeType.FOREVER;

	@AllArgsConstructor
	@Getter
	public enum ExpiredTimeType {
		/**
		 * 随机时间，推荐设置
		 */
		RANDOM,

		/**
		 * 固定时间，有缓存雪崩的风险
		 */
		FIXED,

		/**
		 * 默认设置，永久有效
		 */
		FOREVER;

	}
}
