/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.rabbitmq.web;

import com.legendshop.common.rabbitmq.constants.AmqpConst;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@RestController
public class AmqpTestController {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@GetMapping("/mq/test")
	public String hi(String msg) {
		rabbitTemplate.convertAndSend(AmqpConst.MESSAGE_EXCHANGE, AmqpConst.MESSAGE_ROUTING_KEY, msg);
		return msg;
	}
}
