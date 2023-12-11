/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.rabbitmq.constants;

/**
 * 消息队列常量
 *
 * @author legendshop
 */
public interface AmqpConst {

	// ------------------------ 消息推送分隔符-------------------------------------

	String MESSAGE_EXCHANGE = "legendshop.message.exchange";

	String MESSAGE_QUEUE = "legendshop.message.push.queue";

	String MESSAGE_ROUTING_KEY = "message.send";

	//------------------------消息推送结束分隔符-----------------------------------


	// ------------------------ 消息推送分隔符-------------------------------------

	String SYSTEM_LOG_EXCHANGE = "legendshop.system.log.exchange";

	String SYSTEM_LOG_QUEUE = "legendshop.message.log.queue";

	String SYSTEM_LOG_LOGIN_QUEUE = "legendshop.message.log.login.queue";

	String SYSTEM_LOG_LOGIN_ROUTING_KEY = "login.log.save";

	String SYSTEM_LOG_ROUTING_KEY = "system.log.save";

	//------------------------消息推送结束分隔符-----------------------------------


	// ------------------------ 消息推送分隔符-------------------------------------

	String OPERATOR_LOG_EXCHANGE = "legendshop.operator.log.exchange";

	String OPERATOR_LOG_QUEUE = "legendshop.operator.log.queue";

	String OPERATOR_LOG_ROUTING_KEY = "operator.log.save";


	// ------------------------ 导出Excel下载 推送分隔符-------------------------------------

	String SYSTEM_EXPORT_EXCEL_EXCHANGE = " legendshop.system.export.excel.exchange";

	String SYSTEM_EXPORT_EXCEL_UPDATE_QUEUE = "legendshop.system.export.excel.update.queue";

	String SYSTEM_EXPORT_EXCEL_UPDATE_ROUTING_KEY = "legendshop.system.export.excel.update";


	//------------------------消息推送结束分隔符-----------------------------------


	//---------------------------物流消息分隔符-------------------------------------

	String LOGISTIC_EXCHANGE = "legendshop.logistic.exchange";

	String LOGISTIC_SUBSCRIBE_QUEUE = "legendshop.logistic.subscribe.queue";

	String LOGISTIC_SUBSCRIBE_ROUTING_KEY = "logistic.subscribe";

	//------------------------退货物流消息分隔符----------------------------------------

	String REFUND_LOGISTIC_SUBSCRIBE_QUEUE = "legendshop.refund.logistic.subscribe.queue";

	String REFUND_LOGISTIC_SUBSCRIBE_ROUTING_KEY = "refund.logistic.subscribe";

	//------------------------订阅物流分隔符----------------------------------------


	// ------------------------ 下单分隔符-------------------------------------

	/**
	 * 订单下单交换机
	 */
	String ORDER_EXCHANGE = "legendshop.order.exchange";

	/**
	 * 下单保存订单历史队列
	 */
	String ORDER_SAVE_HISTORY_QUEUE = "legendshop.order.save.history.queue";

	/**
	 * 下单扣减库存队列
	 */
	String ORDER_DEDUCTION_STOCK_QUEUE = "legendshop.order.deduction.stock.queue";

	/**
	 * 下单保存商品快照队列
	 */
	String ORDER_SAVE_PRODUCT_SNAPSHOT_QUEUE = "legendshop.order.save.product.snapshot.queue";

	/**
	 * 下单保存订单历史路由键
	 */
	String ORDER_SAVE_HISTORY_ROUTING_KEY = "order.save.history";

	/**
	 * 下单扣减库存路由键
	 */
	String ORDER_DEDUCTION_STOCK_ROUTING_KEY = "order.deduction.stock";

	/**
	 * 下单保存商品快照路由键
	 */
	String ORDER_SAVE_PRODUCT_SNAPSHOT_ROUTING_KEY = "order.save.product.snapshot";

	//------------------------下单分隔符-----------------------------------


	/**
	 * 延迟队列交换机
	 */
	String DELAY_EXCHANGE = "legendshop.delay.exchange";

	/**
	 * 死信交换机 x-dead-letter-exchange
	 */
	String DEAD_EXCHANGE = "x-dead-letter-exchange";
	String DEAD_EXCHANGE_KEY = "x-dead-letter-routing-key";

	//----------------------商品预约上架分隔符-----------------------------------

	String DELAY_APPOINT_ONLINE_QUEUE = "legendshop.delay.productAppointOnline.queue";

	String DELAY_APPOINT_ONLINE_ROUTING_KEY = "delay.productAppointOnline";

	//----------------------商品预约上架分隔符-----------------------------------

	//----------------------订单自动确认收货分隔符-----------------------------------

	String DELAY_CONFIRMATION_RECEIVING_QUEUE = "legendshop.delay.OrderOfConfirmationReceiving.queue";

	String DELAY_CONFIRMATION_RECEIVING_ROUTING_KEY = "delay.confirmationOfReceiving";

	//----------------------订单自动确认收货分隔符-----------------------------------

	//----------------------退货订单自动确认收货分隔符-----------------------------------

	String DELAY_REFUND_CONFIRM_DELIVERY_QUEUE = "legendshop.delay.RefundConfirmDelivery.queue";

	String DELAY_REFUND_CONFIRM_DELIVERY_ROUTING_KEY = "delay.RefundConfirmDelivery";

	//----------------------退货订单自动确认收货分隔符-----------------------------------

	//----------------------订单自动同意售后分隔符-----------------------------------

	String DELAY_AGREE_REFUND_QUEUE = "legendshop.delay.OrderOfAgreeRefund.queue";

	String DELAY_AGREE_REFUND_ROUTING_KEY = "delay.agreeRefund";

	//----------------------订单自动同意售后分隔符-----------------------------------

	//----------------------待评价订单转订单完成分隔符-----------------------------------

	String DELAY_TREAT_COMMENT_QUEUE = "legendshop.delay.OrderofTreatComment.queue";

	String DELAY_TREAT_COMMENT_ROUTING_KEY = "delay.treatcomment";

	//----------------------待评价订单转订单完成分隔符-----------------------------------


	//----------------------平台订单自动同意售后分隔符-----------------------------------

	String DELAY_ADMIN_AGREE_REFUND_QUEUE = "legendshop.delay.admin.OrderOfAgreeRefund.queue";

	String DELAY_ADMIN_AGREE_REFUND_ROUTING_KEY = "delay.admin.agreeRefund";

	//----------------------平台订单自动同意售后分隔符-----------------------------------

	//----------------------订单自动取消售后分隔符-----------------------------------

	String DELAY_CANCEL_REFUND_QUEUE = "legendshop.delay.OrderOfCancelRefund.queue";

	String DELAY_CANCEL_REFUND_ROUTING_KEY = "delay.cancelRefund";

	//----------------------订单自动取消售后分隔符-----------------------------------


	//----------------------自动取消超时未支付订单分隔符-----------------------------------

	String DELAY_CANCEL_UNPAY_ORDER_QUEUE = "legendshop.delay.cancel.unPay.order.queue";

	String DELAY_CANCEL_UNPAY_ORDER_ROUTING_KEY = "delay.cancel.unPay.order";

	String DELAY_CANCEL_UNPAY_ORDER_QUEUE_BEFORE = "legendshop.delay.cancel.unPay.order.before.queue";

	String DELAY_CANCEL_UNPAY_ORDER_ROUTING_KEY_BEFORE = "delay.cancel.unPay.order.before";

	//----------------------自动取消超时未支付订单分隔符-----------------------------------

	//----------------------预售订单尾款未支付订单-----------------------------------

	String DELAY_CANCEL_UNPAID_PRE_SELL_ORDER_QUEUE = "legendshop.delay.cancel.unpaid.pre.sell.order.queue";

	String DELAY_CANCEL_UNPAID_PRE_SELL_ORDER_ROUTING_KEY = "delay.cancel.unpaid.pre.sell.order";

	//----------------------预售订单尾款未支付订单-----------------------------------

	//----------------------预售订单尾款未支付订单-----------------------------------

	String DELAY_PRE_SELL_ORDER_FINAL_PAYMENT_NOTICE_QUEUE = "legendshop.delay.pre.sell.order.final.payment.notice.queue";

	String DELAY_PRE_SELL_ORDER_FINAL_PAYMENT_NOTICE_ROUTING_KEY = "delay.pre.sell.order.final.payment.notice";

	//----------------------预售订单尾款未支付订单-----------------------------------

	//----------------------优惠券分隔符--------------------------------------

	/**
	 * 队列名称：超时删除无效的优惠券商品
	 */
	String COUPON_DELAY_DELETE_QUEUE = "legendshop.delay.coupon.delete.queue";
	String COUPON_DELAY_DELETE_ROUTING_KEY = "delay.coupon.delete";
	/**
	 * 队列名称：超时删除平台优惠券
	 */
	String COUPON_DELAY_DELETE_PLATFORM_QUEUE = "legendshop.delay.coupon.delete.platform.queue";
	String COUPON_DELAY_DELETE_PLATFORM_ROUTING_KEY = "delay.coupon.delete.platform";
	/**
	 * 队列名称：【优惠券】到点生效
	 */
	String DELAY_COUPON_ONLINE_QUEUE = "legendshop.delay.coupon.online.queue";
	String DELAY_COUPON_ONLINE_ROUTING_KEY = "delay.coupon.online";
	/**
	 * 队列名称：【优惠券】到点失效
	 */
	String DELAY_COUPON_OFFLINE_QUEUE = "legendshop.delay.coupon.offline.queue";
	String DELAY_COUPON_OFFLINE_ROUTING_KEY = "delay.offline.online";
	/**
	 * 队列名称：【用户优惠券】到点生效
	 */
	String DELAY_USER_COUPON_ONLINE_QUEUE = "legendshop.delay.usercoupon.online.queue";
	String DELAY_USER_COUPON_ONLINE_ROUTING_KEY = "delay.usercoupon.online";
	/**
	 * 队列名称：【用户优惠券】到点失效
	 */
	String DELAY_USER_COUPON_OFFLINE_QUEUE = "legendshop.delay.usercoupon.offline.queue";
	String DELAY_USER_COUPON_OFFLINE_ROUTING_KEY = "delay.usercoupon.offline";
	//----------------------优惠券分隔符--------------------------------------


	//----------------------广告分隔符-----------------------------------
	/**
	 * 商品服务 广告延时队列
	 */
	String LEGENDSHOP_SHOP_ADVERTISE_END_QUEUE = "legendshop.shop.advertise.end.queue";

	String LEGENDSHOP_SHOP_ADVERTISE_LOG_END_ROUTING_KEY = "legendshop.shop.advertise.end.log";
	String LEGENDSHOP_SHOP_ADVERTISE_START_QUEUE = "legendshop.shop.advertise.start.queue";

	String LEGENDSHOP_SHOP_ADVERTISE_LOG_START_ROUTING_KEY = "legendshop.shop.advertise.start.log";

}
