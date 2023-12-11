/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.constants;

/**
 * 消息队列常量
 *
 * @author legendshop
 */
public interface AmqpConst {

	/**
	 * 商品服务点击广告exchange
	 */
	String LEGENDSHOP_SHOP_CLICK_EXCHANGE = "legendshop.shop.click.exchange";

	/**
	 * 商品服务点击广告exchange
	 */
	String LEGENDSHOP_SHOP_PUT_EXCHANGE = "legendshop.shop.put.exchange";

	/**
	 * 商品服务 点击广告统计队列
	 */
	String LEGENDSHOP_SHOP_ADVERTISE_CLICK_QUEUE = "legendshop.shop.advertise.click.queue";

	String LEGENDSHOP_SHOP_ADVERTISE_LOG_CLICK_ROUTING_KEY = "legendshop.shop.advertise.put.log";

	/**
	 * 商品服务 推送广告统计队列
	 */
	String LEGENDSHOP_SHOP_ADVERTISE_PUT_QUEUE = "legendshop.shop.advertise.put.queue";

	String LEGENDSHOP_SHOP_ADVERTISE_PUT_LOG_ROUTING_KEY = "legendshop.shop.advertise.put.log";

}
