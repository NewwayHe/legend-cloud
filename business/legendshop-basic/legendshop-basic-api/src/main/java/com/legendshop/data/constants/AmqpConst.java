/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.constants;

/**
 * 消息队列常量
 *
 * @author legendshop
 */
public interface AmqpConst {

	/**
	 * 数据服务exchange
	 */
	String LEGENDSHOP_DATA_EXCHANGE = "legendshop.data.exchange";

	/**
	 * 优惠券数据服务exchange
	 */
	String COUPON_DATA_EXCHANGE = "legendshop.coupon.data.exchange";

	/**
	 * 数据服务 搜索日志队列
	 */
	String LEGENDSHOP_DATA_SEARCH_QUEUE = "legendshop.data.search.queue";

	String LEGENDSHOP_DATA_SEARCH_LOG_ROUTING_KEY = "legendshop.data.search.log";

	/**
	 * 数据服务 订单统计队列
	 */
	String LEGENDSHOP_DATA_ORDER_QUEUE = "legendshop.data.order.queue";

	String LEGENDSHOP_DATA_ORDER_LOG_ROUTING_KEY = "legendshop.data.order.log";

	/**
	 * 数据服务 商品浏览统计队列
	 */
	String LEGENDSHOP_DATA_PRODUCT_VIEW_QUEUE = "legendshop.data.product.view.queue";

	String LEGENDSHOP_DATA_PRODUCT_VIEW_LOG_ROUTING_KEY = "legendshop.data.product.view.log";

	/**
	 * 数据服务 店铺访问记录队列
	 */
	String LEGENDSHOP_DATA_SHOP_VIEW_QUEUE = "legendshop.data.shop.view.queue";

	String LEGENDSHOP_DATA_SHOP_VIEW_LOG_ROUTING_KEY = "legendshop.data.shop.view.log";

	/**
	 * 数据服务 商品浏览统计队列 加入购物车数据统计
	 */
	String LEGENDSHOP_DATA_PRODUCT_VIEW_CART_QUEUE = "legendshop.data.product.view.cart.queue";

	String LEGENDSHOP_DATA_PRODUCT_VIEW_CART_LOG_ROUTING_KEY = "legendshop.data.product.view.cart.log";

	/**
	 * 数据服务 活动浏览统计队列
	 */
	String LEGENDSHOP_DATA_ACTIVITY_VIEW_QUEUE = "legendshop.data.activity.view.queue";

	String LEGENDSHOP_DATA_ACTIVITY_VIEW_LOG_ROUTING_KEY = "legendshop.data.activity.view.log";

	/**
	 * 数据服务 活动优惠券浏览统计队列
	 */
	String LEGENDSHOP_DATA_COUPON_VIEW_QUEUE = "legendshop.data.activity.coupon.view.queue";

	String LEGENDSHOP_DATA_COUPON_VIEW_LOG_ROUTING_KEY = "legendshop.data.activity.coupon.view.log";

}
