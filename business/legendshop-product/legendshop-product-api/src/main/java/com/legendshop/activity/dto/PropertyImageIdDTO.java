/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 属性图片ID
 *
 * @author legendshop
 */
@Data
public class PropertyImageIdDTO implements Serializable {

	private static final long serialVersionUID = -6930669929367596123L;

	private Long id;

	private String name;

	public PropertyImageIdDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public PropertyImageIdDTO(Long id) {
		this.id = id;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PropertyImageIdDTO other = (PropertyImageIdDTO) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
