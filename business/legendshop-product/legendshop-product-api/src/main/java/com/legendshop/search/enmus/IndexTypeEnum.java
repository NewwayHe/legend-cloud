/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.enmus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum IndexTypeEnum {

	ALL_INDEX(0, "全部索引"),

	/**
	 * 商品索引
	 */
	PRODUCT_INDEX(1, "商品索引"),

	/**
	 * 店铺索引
	 */
	SHOP_INDEX(2, "店铺索引"),

	/**
	 * 营销索引
	 */
	ACTIVITY_INDEX(3, "营销索引"),

	/**
	 * 优惠券索引
	 */
	COUPON_INDEX(4, "优惠券索引"),
	;


	private Integer value;
	private String indexTypeName;

	public static Boolean isExist(Integer type) {
		IndexTypeEnum[] values = values();
		for (IndexTypeEnum index : values) {
			if (index.getValue().equals(type)) {
				return true;
			}
		}
		return false;
	}
}
