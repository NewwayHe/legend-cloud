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
public class KeyValueEntityDTO implements Serializable, Cloneable {

	private static final long serialVersionUID = 8850258001809193742L;
	/**
	 * 键
	 */
	@ApiModelProperty(value = "键")
	private String key;

	/**
	 * value
	 */
	@ApiModelProperty(value = "键值")
	private String value = "";

	public KeyValueEntityDTO() {

	}

	public KeyValueEntityDTO(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public KeyValueEntityDTO(Integer key, String value) {
		this.key = String.valueOf(key);
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * hash
	 *
	 * @return
	 */
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + key.hashCode();
		result = 31 * result + value.hashCode();
		return result;
	}

	/**
	 * 比较
	 *
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof KeyValueEntityDTO) {
			KeyValueEntityDTO entity = (KeyValueEntityDTO) obj;
			if (entity.getKey() != null && entity.getValue() != null && entity.getKey().equals(this.key)
					&& entity.getValue().equals(this.value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 克隆
	 *
	 * @return
	 */
	@Override
	public KeyValueEntityDTO clone() {
		KeyValueEntityDTO entity = new KeyValueEntityDTO();
		entity.setKey(this.getKey());
		entity.setValue(this.getValue());

		return entity;
	}
}
