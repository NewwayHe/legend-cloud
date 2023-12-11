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
 * 分销队列常量
 *
 * @author legendshop
 * @create: 2021/9/13 17:53
 */
public interface RabbitConstants {


	// ========================= 用户钱包

	/**
	 * 用户钱包交换机
	 */
	String USER_WALLET_EXCHANGE = "legendshop.user.wallet.exchange";

	String USER_WALLET_CENTRE_QUEUE = "legendshop.user.wallet.centre.queue";

	String USER_WALLET_CENTRE = "user.wallet.centre";

}
