/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.enums;


import com.legendshop.common.core.enums.IntegerEnum;

/**
 * 商品包邮类型枚举
 *
 * @author legendshop
 */
public enum ShippingTypeEnum implements IntegerEnum {

	/**
	 * 店铺包邮
	 */
	SHOP(0),

	/**
	 * 商品包邮
	 */
	PROD(1),


	/**
	 * 满件
	 */
	FULL_PIECE(2),

	/**
	 * 满金额
	 */
	FULL_MONEY(1),

	/**
	 * 下线
	 */
	OFFLINE(0),

	/**
	 * 上线
	 */
	ONLINE(1),
	;
	private Integer type;

	ShippingTypeEnum(Integer type) {
		this.type = type;
	}

	@Override
	public Integer value() {
		return type;
	}

}
