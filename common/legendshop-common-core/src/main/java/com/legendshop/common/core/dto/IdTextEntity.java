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

/**
 * Key Value Mapping.
 *
 * @author legendshop
 */
public class IdTextEntity implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 8762806056619273121L;

	/**
	 *
	 */

	private String id;

	private String text;

	public IdTextEntity() {

	}

	public IdTextEntity(String id, String text) {
		this.id = id;
		this.text = text;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
