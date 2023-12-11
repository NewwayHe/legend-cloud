/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 产品删除状态.
 * -2：永久删除；-1：删除；1：正常；
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum ProductDelStatusEnum {

	/**
	 * 正常（默认状态）.
	 */
	PROD_NORMAL(1),

	/**
	 * 商品删除状态 (放商品回收站)
	 **/
	PROD_DELETE(-1),

	/**
	 * 商家永久删除状态
	 **/
	PROD_SHOP_DELETE(-2),

	/**
	 * 平台永久删除（物理删除）
	 **/
	PROD_ADMIN_DELETE(-3),
	;

	private Integer value;

	public Integer value() {
		return this.value;
	}

	public static ProductDelStatusEnum fromCode(Integer value) {
		if (value != null) {
			for (ProductDelStatusEnum typeEnum : ProductDelStatusEnum.values()) {
				if (value.equals(typeEnum.value)) {
					return typeEnum;
				}
			}
		}
		return null;
	}
}
