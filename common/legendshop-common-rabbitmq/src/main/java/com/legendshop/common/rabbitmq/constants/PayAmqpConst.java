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
 * @author legendshop
 */
public interface PayAmqpConst {

	//------------------------支付分隔符-----------------------------------

	/**
	 * 支付交换机
	 */
	String PAY_EXCHANGE = "legendshop.pay.exchange";

	/**
	 * 支付异常记录
	 */
	String PAY_EXCEPTION_HISTORY_QUEUE = "legendshop.pay.exception.history.queue";

	/**
	 * 下单保存订单历史路由键
	 */
	String PAY_EXCEPTION_HISTORY_ROUTING_KEY = "pay.exception.history";

	/**
	 * 支付成功更新商品购买数
	 */
	String UPDATE_BUYS_QUEUE = "legendshop.delay.updateBuys.queue";
	String UPDATE_BUYS_ROUTING_KEY = "delay.updateBuys";


	/**
	 * 提现处理
	 */
	String USER_WITHDRAW_QUEUE = "legendshop.delay.user.withdraw.queue";

	String USER_WITHDRAW_ROUTING_KEY = "user.withdraw";

	String DISTRIBUTION_WITHDRAW_QUEUE = "legendshop.delay.distribution.withdraw.queue";

	String DISTRIBUTION_WITHDRAW_ROUTING_KEY = "distribution.withdraw";

	//------------------------支付分隔符-----------------------------------
}
