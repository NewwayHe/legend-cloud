/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.mq.producer;

/**
 * 支付提供者
 *
 * @author: jzh
 * @create: 2021/4/1 18:29
 */

import com.legendshop.order.dto.OrderDTO;
import com.legendshop.order.dto.PreSellOrderDTO;

/**
 * @author legendshop
 */
public interface PayProducerService {

	/**
	 * 更新商品购买数
	 *
	 * @param numberListJsonStr
	 */
	void updateBuys(String numberListJsonStr);

	/**
	 * 发送用户购买力支付数据到data
	 *
	 * @param orderDTO
	 */
	void sendPayMessage(OrderDTO orderDTO);

	/**
	 * 发送易宝注册入网请求
	 *
	 * @param shopId
	 */
	void yeepayRegister(Long shopId);

	/**
	 * 处理预售尾款延时处理
	 *
	 * @param orderDTO
	 */
	void handlePreSellFinalPaymentTimeOut(PreSellOrderDTO orderDTO);

	/**
	 * 处理预售尾款到期支付站内信通知用户处理
	 *
	 * @param orderDTO
	 */
	void handlePreSellFinalPaymentNotice(PreSellOrderDTO orderDTO);


}
