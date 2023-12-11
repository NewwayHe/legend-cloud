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
 * 104=服务端返回/营销活动模块
 * 01：拍卖
 * 02：优惠券
 * 03：满减满折
 * 04：拼团
 * 05：预售
 * 06：秒杀
 * 07：包邮
 * 08 团购
 * 09：门店
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum ActivityResponseEnum implements BusinessExceptionAssert {

	/**
	 * 拍卖活动找不到
	 */
	AUCTION_NOT_FOUNT_EXCEPTION(10401001, "没有找到该拍卖"),

	/**
	 * 团购活动找不到
	 */
	GROUP_NOT_FOUNT_EXCEPTION(10408001, "没有找到该拍卖"),

	/**
	 * 团购活动状态异常
	 */
	GROUP_STATUS_EXCEPTION(10408002, "团购活动状态失效，请重新提交订单"),

	/**
	 * 门店活动状态异常
	 */
	STORE_STATUS_EXCEPTION(10409002, "团购活动状态失效，请重新提交订单"),

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
