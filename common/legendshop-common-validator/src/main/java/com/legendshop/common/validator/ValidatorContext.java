/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.validator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证器在执行调用过程中的上下文
 * 1.验证器中的数据传递共享
 * 2.验证结果数据缓存以作后续使用
 *
 * @author legendshop
 */
public class ValidatorContext {
	/**
	 * 验证器均可以共享使用的属性键值对
	 */
	private Map<String, Object> attributes;


	/**
	 * 设置属性值
	 *
	 * @param key   键
	 * @param value 值
	 */
	public void setAttribute(String key, Object value) {
		if (attributes == null) {
			attributes = new HashMap<>(16);
		}
		attributes.put(key, value);
	}


	/**
	 * 获取String值
	 *
	 * @param key
	 * @return
	 */
	public String getString(String key) {

		return (String) getAttribute(key);
	}

	/**
	 * 获取Integer值
	 *
	 * @param key
	 * @return
	 */
	public Integer getInteger(String key) {
		return (Integer) getAttribute(key);
	}

	/**
	 * 获取Boolean值
	 *
	 * @param key
	 * @return
	 */
	public Boolean getBoolean(String key) {
		return (Boolean) getAttribute(key);
	}


	/**
	 * 获取Long值
	 *
	 * @param key
	 * @return
	 */
	public Long getLong(String key) {
		return (Long) getAttribute(key);
	}

	/**
	 * 获取BigDecimal值
	 *
	 * @param key
	 * @return
	 */
	public BigDecimal getBigDecimal(String key) {
		return (BigDecimal) getAttribute(key);
	}

	/**
	 * 获取对象
	 *
	 * @param key
	 * @param <T>
	 * @return
	 */
	public <T> T getClazz(String key) {
		return (T) getAttribute(key);
	}

	/**
	 * 获取属性
	 *
	 * @param key 键
	 * @return 值
	 */
	public Object getAttribute(String key) {
		if (attributes != null && !attributes.isEmpty()) {
			return attributes.get(key);
		}
		return null;
	}
}
