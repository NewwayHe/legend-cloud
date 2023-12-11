/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;

import lombok.Getter;

/**
 * 系统配置分组
 *
 * @author legendshop
 */
@Getter
public enum SysParamGroupEnum {

	//----------------------第三方配置---------------------------------------------------

	/**
	 * 微信
	 */
	WX("微信配置"),

//	/**
//	 * 阿里
//	 */
//	ALI("阿里配置"),


//	/**
//	 * qq
//	 */
//	QQ("QQ配置"),


	/**
	 * 消息推送
	 */
	PUSH("消息推送配置"),


	/**
	 * 物流
	 */
	LOGISTICS("物流配置"),


	//业务配置------------------------------分隔符------------------------------------

	/**
	 * 店铺
	 */
	SHOP("店铺配置"),

	/**
	 * 订单
	 */
	ORDER("订单配置"),

	/**
	 * 支付
	 */
	PAY("支付配置"),

	/**
	 * 分账支付
	 */
	SUB_PAY("分账支付配置"),


	//系统配置------------------------------分隔符------------------------------------


	/**
	 * 系统
	 */
	SYS("系统配置"),


//	/**
//	 * 日志
//	 */
//	LOG("日志配置")
	;

	final String desc;

	SysParamGroupEnum(String desc) {
		this.desc = desc;
	}
}
