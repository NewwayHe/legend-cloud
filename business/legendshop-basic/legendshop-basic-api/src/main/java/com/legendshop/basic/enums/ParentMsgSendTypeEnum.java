/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统通知父类型
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum ParentMsgSendTypeEnum {

	/**
	 * 商品通知
	 */
	PRODUCT(1, "商品通知"),

	/**
	 * 订单通知
	 */
	ORDER(2, "订单通知"),

	/**
	 * 售后通知
	 */
	AFTER_SALE(3, "售后通知"),

	/**
	 * 其他通知
	 */
	OTHER(0, "其他通知");

	private final Integer value;
	private final String des;


}
