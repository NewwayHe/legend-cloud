/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.expetion.enums;


import com.legendshop.common.core.expetion.BusinessExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 106=服务端返回/营销活动模块
 * 01：订单支付
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum OrderResponseEnum implements BusinessExceptionAssert {


	/**
	 * 下单失败
	 */
	ORDER_FAILED_EXCEPTION(10601000, "下单失败"),

	/**
	 * 重复支付的订单
	 */
	REPEAT_PAY_ORDER_EXCEPTION(10601001, "请勿重复支付订单"),


	;

	/**
	 * 返回码
	 */
	private final int code;
	/**
	 * 返回消息
	 */
	private final String msg;
}
