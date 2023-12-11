/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.mq;

import com.legendshop.common.core.dto.SystemLogDTO;
import com.legendshop.common.rabbitmq.constants.AmqpConst;
import com.legendshop.user.service.LoginHistoryService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 用户登录历史监听器
 *
 * @author legendshop
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginHistoryListener {

	private final LoginHistoryService loginHistoryService;

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.SYSTEM_LOG_LOGIN_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.SYSTEM_LOG_EXCHANGE), key = {AmqpConst.SYSTEM_LOG_LOGIN_ROUTING_KEY}
	))
	@SneakyThrows
	public void loginListener(SystemLogDTO systemLogDTO, Message message, Channel channel) {

		try {
			this.loginHistoryService.saveLoginHistory(systemLogDTO, 1);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
	}
}
