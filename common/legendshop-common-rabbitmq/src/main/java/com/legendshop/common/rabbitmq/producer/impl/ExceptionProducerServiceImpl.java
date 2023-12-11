/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.rabbitmq.producer.impl;

import com.legendshop.common.rabbitmq.constants.PayAmqpConst;
import com.legendshop.common.rabbitmq.producer.ExceptionProducerService;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author legendshop
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExceptionProducerServiceImpl implements ExceptionProducerService {

	final AmqpSendMsgUtil amqpSendMsgUtil;

	@Override
	public void payException(String objectJson) {
		amqpSendMsgUtil.convertAndSend(PayAmqpConst.PAY_EXCHANGE, PayAmqpConst.PAY_EXCEPTION_HISTORY_ROUTING_KEY, objectJson);
	}
}
