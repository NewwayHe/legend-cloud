/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.data.cache.util;

import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * CacheManager工具类
 *
 * @author legendshop
 */
@Component
@AllArgsConstructor
public class CacheManagerUtil {

	private final CacheManager cacheManager;

	/**
	 * 获取缓存
	 *
	 * @param cacheName
	 * @param key
	 * @param <T>
	 * @return
	 */
	public <T> T getCache(String cacheName, Object key) {
		Cache cache = cacheManager.getCache(cacheName);
		if (null == cache) {
			return null;
		}
		Cache.ValueWrapper valueWrapper = cache.get(key);
		if (null == valueWrapper) {
			return null;
		}
		return (T) valueWrapper.get();
	}


	/**
	 * 设置缓存
	 *
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	public void putCache(String cacheName, Object key, Object value) {
		Cache cache = cacheManager.getCache(cacheName);
		if (cache != null) {
			cache.put(key, value);
		}
	}

	/**
	 * 清除缓存
	 *
	 * @param cacheName
	 * @param key
	 */
	public void evictCache(String cacheName, Object key) {
		Cache cache = cacheManager.getCache(cacheName);
		if (cache != null) {
			cache.evict(key);
		}
	}

	/**
	 * 清除缓存
	 *
	 * @param cacheName
	 */
	public void clear(String cacheName) {
		Cache cache = cacheManager.getCache(cacheName);
		if (cache != null) {
			cache.clear();
		}
	}
}
