/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.mq;

import com.legendshop.pay.constants.RabbitConstants;
import com.legendshop.pay.service.UserWalletBusinessService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 钱包队列监听者
 *
 * @author legendshop
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WalletListener {

	private final UserWalletBusinessService userWalletBusinessService;


	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = RabbitConstants.USER_WALLET_CENTRE_QUEUE, durable = "true"),
			exchange = @Exchange(value = RabbitConstants.USER_WALLET_EXCHANGE), key = {RabbitConstants.USER_WALLET_CENTRE}
	))
	public void userConsumptionCentre(Long centreId, Message message, Channel channel) throws IOException {
		log.info("userConsumptionCentre 进入中间表消费队列，centreId：{}", centreId);

		try {
			userWalletBusinessService.consumptionCentre(centreId);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
	}
}
