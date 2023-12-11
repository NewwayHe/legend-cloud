/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
public class IdNameImgDTO implements Serializable {

	private static final long serialVersionUID = 3957187735144172121L;


	private Long id;

	private String name;

	private String image;


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

		IdNameImgDTO other = (IdNameImgDTO) obj;
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
