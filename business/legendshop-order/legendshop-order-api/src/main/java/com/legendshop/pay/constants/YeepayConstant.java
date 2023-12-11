/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.constants;

/**
 * 易宝支付常量
 *
 * @author legendshop
 * @create: 2021/4/8 11:44
 */
public interface YeepayConstant {

	// ========================== 易宝支付队列 ==========================

	/**
	 * 易宝支付交换机
	 */
	String YEEPAY_EXCHANGE = "legendshop.yeepay.exchange";


	/**
	 * 易宝入网进件队列
	 */
	String YEEPAY_SHOP_INCOMING_QUERY = "legendshop.yeepay.shop.incoming.query";


	/**
	 * 易宝入网进件注册路由键
	 */
	String YEEPAY_SHOP_INCOMING_REGISTER_ROUTING_KEY = "yeepay.shop.incoming.register";


}
