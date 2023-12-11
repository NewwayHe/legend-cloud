/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.enums;

import com.legendshop.common.core.dto.KeyValueEntity;

/**
 * 带有状态位的键值对对象.
 *
 * @author legendshop
 */
public class StatusKeyValueEntity extends KeyValueEntity {

	private static final long serialVersionUID = 1041724257946499371L;

	/**
	 * status = selected?. for select  option
	 */
	private String status;

	public StatusKeyValueEntity() {
		super();
	}

	public StatusKeyValueEntity(String key, String value) {
		super(key, value);
	}

	public StatusKeyValueEntity(Integer key, String value) {
		super(String.valueOf(key), value);
	}

	public StatusKeyValueEntity(Long key, String value) {
		super(String.valueOf(key), value);
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
