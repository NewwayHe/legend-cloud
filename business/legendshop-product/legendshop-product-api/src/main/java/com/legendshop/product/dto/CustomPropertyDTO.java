/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 客户自定义属性.
 *
 * @author legendshop
 */
public class CustomPropertyDTO implements Serializable, Cloneable {


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
	public CustomPropertyDTO() {

	}

	/**
	 * Instantiates a new key value entity.
	 *
	 * @param key   the key
	 * @param value the value
	 */
	public CustomPropertyDTO(String key, List<String> value) {
		this.key = key;
		this.value = value;
	}

	public CustomPropertyDTO(Integer key, List<String> value) {
		this.key = String.valueOf(key);
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<String> getValue() {
		return value;
	}

	public void setValue(List<String> value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + key.hashCode();
		result = 31 * result + value.hashCode();
		return result;
	}


	@Override
	public CustomPropertyDTO clone() {
		CustomPropertyDTO entity = new CustomPropertyDTO();

		entity.setKey(this.getKey());
		entity.setValue(this.getValue());

		return entity;
	}
}
