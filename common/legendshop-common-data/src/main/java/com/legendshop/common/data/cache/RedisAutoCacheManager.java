/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.data.cache;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.legendshop.common.data.cache.properties.RedisCacheProperties;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * redis缓存管理类
 *
 * @author legendshop
 */
public class RedisAutoCacheManager extends RedisCacheManager {

	/**
	 * 分隔符号
	 */
	private static final String SPLIT_FLAG = "#";

	private static final int CACHE_LENGTH = 2;

	private final RedisCacheProperties properties;

	/**
	 * 继承RedisCacheManager来管理createRedisCache方法
	 *
	 * @param cacheWriter                自定义cacheWriter
	 * @param defaultCacheConfiguration
	 * @param initialCacheConfigurations
	 * @param allowInFlightCacheCreation
	 */
	RedisAutoCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration,
						  Map<String, RedisCacheConfiguration> initialCacheConfigurations, RedisCacheProperties properties, boolean allowInFlightCacheCreation) {
		super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, allowInFlightCacheCreation);
		this.properties = properties;
	}

	/**
	 * 每次创建一个缓存都会走createRedisCache
	 * 缓存过期
	 *
	 * @param name        name里面会带有#分隔过期时间ttl
	 * @param cacheConfig
	 * @return
	 */
	@Override
	protected RedisCache createRedisCache(String name, @Nullable RedisCacheConfiguration cacheConfig) {
		Duration duration = Duration.of(expiredTime(properties), properties.getChronoUnit());
		if (StrUtil.isBlank(name) || !name.contains(SPLIT_FLAG)) {
			cacheConfig = settingTtl(cacheConfig, duration);
			return super.createRedisCache(name, cacheConfig);
		}
		//分隔#号
		String[] cacheArray = name.split(SPLIT_FLAG);
		if (cacheArray.length < CACHE_LENGTH) {
			cacheConfig = settingTtl(cacheConfig, duration);
			return super.createRedisCache(name, cacheConfig);
		}
		//设置过期时间
		duration = DurationStyle.detectAndParse(cacheArray[1], ChronoUnit.SECONDS);
		cacheConfig = settingTtl(cacheConfig, duration);
		return super.createRedisCache(cacheArray[0], cacheConfig);
	}

	/**
	 * 设置ttl过期时间
	 *
	 * @param cacheConfig
	 * @param duration
	 * @return
	 */
	private RedisCacheConfiguration settingTtl(RedisCacheConfiguration cacheConfig, Duration duration) {
		if (cacheConfig == null) {
			return null;
		}
		return cacheConfig.entryTtl(duration);
	}

	/**
	 * 计算过期时间
	 *
	 * @param properties
	 * @return
	 */
	private int expiredTime(RedisCacheProperties properties) {
		int expiredTime;
		switch (properties.getExpiredTimeType()) {
			case RANDOM:
				int min = properties.getMin();
				int max = properties.getMax();
				expiredTime = RandomUtil.randomInt(min, max);
				break;
			case FOREVER:
				expiredTime = 0;
				break;
			default:
				expiredTime = properties.getExpiredTime();
				break;
		}
		return expiredTime;
	}
}
