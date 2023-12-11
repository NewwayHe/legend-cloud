/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.enums;


import com.legendshop.common.core.enums.StringEnum;

/**
 * 礼券
 * 礼券提供方：平台: platform，店铺:shop
 *
 * @author legendshop
 */
public enum CouponProviderEnum implements StringEnum {

	/**
	 * 平台
	 */
	PLATFORM("platform", 0),

	/**
	 * 店铺
	 */
	SHOP("shop", 1);

	/**
	 * The num.
	 */
	private String type;

	private Integer values;

	CouponProviderEnum(String type, Integer values) {
		this.type = type;
		this.values = values;
	}

	public Integer getValues() {
		return values;
	}

	public void setValues(Integer values) {
		this.values = values;
	}

	@Override
	public String value() {
		return type;
	}

}
