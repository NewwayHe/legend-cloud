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
 * 礼券的类型
 * 通用券:common，品类券:category，指定商品券:product，指定店铺：shop
 *
 * @author legendshop
 */
public enum CouponTypeEnum implements StringEnum {

	/**
	 * 通用券
	 */
	COMMON("common"),

	/**
	 * 品类券
	 */
	CATEGORY("category"),

	/**
	 * 指定商品券
	 */
	PRODUCT("product"),

	/**
	 * 指定店铺红包
	 */
	SHOP("shop");
	/**
	 * The num.
	 */
	private String type;

	CouponTypeEnum(String type) {
		this.type = type;
	}

	@Override
	public String value() {
		return type;
	}

}
