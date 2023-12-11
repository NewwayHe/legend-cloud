/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.basic.api.ExceptionLogApi;
import com.legendshop.basic.dto.ExceptionLogDTO;
import com.legendshop.common.rabbitmq.constants.PayAmqpConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author legendshop
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PayExceptionRabbitListener {

	final ObjectMapper objectMapper = new ObjectMapper();

	final ExceptionLogApi exceptionLogApi;

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = PayAmqpConst.PAY_EXCEPTION_HISTORY_QUEUE, durable = "true"),
			exchange = @Exchange(value = PayAmqpConst.PAY_EXCHANGE), key = {PayAmqpConst.PAY_EXCEPTION_HISTORY_ROUTING_KEY}
	))
	public void receiveDelay(String objectJson) throws IOException {
		ExceptionLogDTO exceptionLog = this.objectMapper.readValue(objectJson, ExceptionLogDTO.class);
		this.exceptionLogApi.save(exceptionLog);
	}

}
