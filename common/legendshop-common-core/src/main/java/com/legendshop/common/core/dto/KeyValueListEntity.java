/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 键值对封装类, key类型为String, value类型为List<String>.
 *
 * @author legendshop
 */
public class KeyValueListEntity implements Serializable, Cloneable {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 5568358970483740841L;

	/**
	 * The key.
	 */
	private String key;

	/**
	 * The value.
	 */
	private List<String> value;

	/**
	 * Instantiates a new key value entity.
	 */
	public KeyValueListEntity() {

	}

	/**
	 * Instantiates a new key value entity.
	 *
	 * @param key   the key
	 * @param value the value
	 */
	public KeyValueListEntity(String key, List<String> value) {
		this.key = key;
		this.value = value;
	}

	public KeyValueListEntity(Integer key, List<String> value) {
		this.key = String.valueOf(key);
		this.value = value;
	}

	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the key.
	 *
	 * @param key the new key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public KeyValueListEntity clone() {
		KeyValueListEntity entity = new KeyValueListEntity();
		entity.setKey(this.getKey());
		entity.setValue(this.getValue());

		return entity;
	}

	public List<String> getValue() {
		return value;
	}

	public void setValue(List<String> value) {
		this.value = value;
	}
}
