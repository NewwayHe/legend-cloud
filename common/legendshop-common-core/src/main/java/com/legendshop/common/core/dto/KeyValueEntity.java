/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 键值对封装类, key类型为String, value类型为String.
 *
 * @author legendshop
 */
@ApiModel(value = "键值对封装类, key类型为String, value类型为String.")
public class KeyValueEntity implements Serializable, Cloneable {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 5568358970483740841L;

	/**
	 * The key.
	 */
	@ApiModelProperty(value = "键")
	private String key;

	/**
	 * The value.
	 */
	@ApiModelProperty(value = "键值")
	private String value = "";

	/**
	 * Instantiates a new key value entity.
	 */
	public KeyValueEntity() {

	}

	/**
	 * Instantiates a new key value entity.
	 *
	 * @param key   the key
	 * @param value the value
	 */
	public KeyValueEntity(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public KeyValueEntity(Integer key, String value) {
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

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + key.hashCode();
		result = 31 * result + value.hashCode();
		return result;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof KeyValueEntity) {
			KeyValueEntity entity = (KeyValueEntity) obj;
			if (entity.getKey() != null && entity.getValue() != null && entity.getKey().equals(this.key)
					&& entity.getValue().equals(this.value)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public KeyValueEntity clone() {
		KeyValueEntity entity = new KeyValueEntity();
		entity.setKey(this.getKey());
		entity.setValue(this.getValue());

		return entity;
	}
}
