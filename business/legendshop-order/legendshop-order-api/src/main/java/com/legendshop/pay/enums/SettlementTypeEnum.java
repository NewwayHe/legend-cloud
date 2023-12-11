/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum SettlementTypeEnum {

	/**
	 * 普通订单支付
	 */
	ORDINARY_ORDER("ORDINARY_ORDER"),

	/**
	 * 用户预付款充值
	 */
	USER_RECHARGE("USER_RECHARGE"),

	/**
	 * 预售订单：定金支付
	 */
	PRE_SALE_ORDER_DEPOSIT("PRE_SALE_ORDER_DEPOSIT"),

	/**
	 * 预售订单：尾款支付
	 */
	PRE_SALE_ORDER_FINAL("PRE_SALE_ORDER_FINAL"),

	/**
	 * 团购订单
	 */
	GROUP_ORDER("GROUP_ORDER"),

	/**
	 * 秒杀订单
	 */
	SEC_KILL_ORDER("SEC_KILL_ORDER"),

	/**
	 * 拼团订单
	 */
	MERGE_GROUP_ORDER("MERGE_GROUP_ORDER");


	private final String value;

}
