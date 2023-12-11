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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author legendshop
 */
@Slf4j
@Component
@AllArgsConstructor
public class StringRedisUtil {

	private final StringRedisTemplate stringRedisTemplate;

	private final Long PAY_CALLBACK_LOCK_EXPIRE_AT = 5000L;

	public static final String PAY_CALLBACK_LOCK_KEY_PREFIX = "PAY_CALLBACK_LOCK_KEY_PREFIX:";

	/**
	 * 存放string类型
	 *
	 * @param key     key
	 * @param data    数据
	 * @param timeout 超时时间
	 */
	public void setString(String key, String data, Long timeout) {
		if (timeout != null) {
			stringRedisTemplate.opsForValue().set(key, data, timeout, TimeUnit.SECONDS);

		} else {
			stringRedisTemplate.opsForValue().set(key, data);
		}
	}

	/**
	 * @param key      the key
	 * @param data     the 数据
	 * @param timeout  the 超时时间
	 * @param timeUnit the 时间单位
	 */
	public void setString(String key, String data, Long timeout, TimeUnit timeUnit) {
		if (timeout != null) {
			stringRedisTemplate.opsForValue().set(key, data, timeout, timeUnit);
		} else {
			stringRedisTemplate.opsForValue().set(key, data);
		}
	}


	/**
	 * 判断存在key
	 *
	 * @param key the key
	 * @return the 是否存在，可能为空
	 */
	public Boolean hasKey(String key) {
		return stringRedisTemplate.hasKey(key);
	}

	/**
	 * 开启Redis 事务
	 */
	public void begin() {
		// 开启Redis 事务权限
		stringRedisTemplate.setEnableTransactionSupport(true);
		// 开启事务
		stringRedisTemplate.multi();

	}

	/**
	 * 提交事务
	 */
	public void exec() {
		// 成功提交事务
		stringRedisTemplate.exec();
	}

	/**
	 * 回滚Redis 事务
	 */
	public void discard() {
		stringRedisTemplate.discard();
	}

	/**
	 * 存放string类型
	 *
	 * @param key  key
	 * @param data 数据
	 */
	public void setString(String key, String data) {
		setString(key, data, null);
	}

	/**
	 * 根据key查询string类型
	 *
	 * @param key the key
	 * @return String
	 */
	public String getString(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}

	/**
	 * 根据对应的key删除key
	 *
	 * @param key the key
	 */
	public Boolean delKey(String key) {
		return stringRedisTemplate.delete(key);
	}

	/**
	 * 数据锁
	 * 针对同一订单进行分布式锁
	 * redis锁
	 */
	public Boolean paymentLock(String prefix, String key) {
		String lockKey = prefix + key;
		return stringRedisTemplate.execute((RedisCallback<Boolean>) connection -> {
			long expireAt = System.currentTimeMillis() + PAY_CALLBACK_LOCK_EXPIRE_AT + 1;
			Boolean acquire = connection.setNX(lockKey.getBytes(), String.valueOf(expireAt).getBytes());
			if (acquire != null && acquire) {
				return true;
			} else {
				byte[] value = connection.get(lockKey.getBytes());
				if (Objects.nonNull(value) && value.length > 0) {
					long expireTime = Long.parseLong(new String(value));
					if (expireTime < System.currentTimeMillis()) {
						// 如果锁已经过期
						byte[] oldValue = connection.getSet(lockKey.getBytes(), String.valueOf(System.currentTimeMillis()).getBytes());
						// 防止死锁
						return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
					}
				}
			}
			return false;
		});
	}
}
