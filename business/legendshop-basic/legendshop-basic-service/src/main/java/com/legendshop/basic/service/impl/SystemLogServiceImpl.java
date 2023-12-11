/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dao.SystemLogDao;
import com.legendshop.basic.query.SystemLogQuery;
import com.legendshop.basic.service.SystemLogService;
import com.legendshop.basic.service.convert.SystemLogConverter;
import com.legendshop.common.core.dto.SystemLogDTO;
import com.legendshop.common.rabbitmq.constants.AmqpConst;
import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author legendshop
 */
@Service
@AllArgsConstructor
@Slf4j
public class SystemLogServiceImpl implements SystemLogService {

	private final SystemLogDao systemLogDao;

	private final SystemLogConverter converter;


	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.SYSTEM_LOG_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.SYSTEM_LOG_EXCHANGE), key = {AmqpConst.SYSTEM_LOG_ROUTING_KEY}
	))
	@SneakyThrows
	public void systemListener(SystemLogDTO systemLogDTO, Message message, Channel channel) {
		Long id = systemLogDao.save(converter.from(systemLogDTO));
		if (ObjectUtil.isNull(id)) {
			log.warn("系统日志保存失败");
			return;
		}
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
	}

	@Override
	public void save(SystemLogDTO systemLogDTO) {
		systemLogDao.save(converter.from(systemLogDTO));
	}

	@Override
	public PageSupport<SystemLogDTO> page(SystemLogQuery systemLogQuery) {
		return converter.page(systemLogDao.page(systemLogQuery));
	}
}
