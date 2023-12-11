/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.enums;


import com.legendshop.common.core.enums.IntegerEnum;

/**
 * 订单删除状态
 *
 * @author legendshop
 */
public enum OrderDeleteStatusEnum implements IntegerEnum {

	/**
	 * 正常状态.
	 */
	NORMAL(0),

	/**
	 * 删除到回收站.
	 */
	DELETED(1),

	/**
	 * 永久删除.
	 */
	PERMANENT_DELETED(2);


	/**
	 * The num.
	 */
	private Integer num;

	@Override
	public Integer value() {
		return num;
	}

	/**
	 * Instantiates a new order status enum.
	 *
	 * @param num the num
	 */
	OrderDeleteStatusEnum(Integer num) {
		this.num = num;
	}

}
