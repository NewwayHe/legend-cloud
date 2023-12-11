/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.rabbitmq.producer.impl;

import com.legendshop.common.rabbitmq.constants.AmqpConst;
import com.legendshop.common.rabbitmq.producer.MessageProducerService;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author legendshop
 */
@Slf4j
@Service
public class MessageProducerServiceImpl implements MessageProducerService {

	@Autowired
	private AmqpSendMsgUtil amqpSendMsgUtil;


	@Override
	public void sendMessage(String msgSendDtoListJsonStr) {
		amqpSendMsgUtil.convertAndSend(AmqpConst.MESSAGE_EXCHANGE, AmqpConst.MESSAGE_ROUTING_KEY, msgSendDtoListJsonStr);
	}
}
