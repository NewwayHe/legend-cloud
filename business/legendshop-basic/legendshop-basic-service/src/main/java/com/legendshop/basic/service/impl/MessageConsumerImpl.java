/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.legendshop.basic.dto.MsgSendDTO;
import com.legendshop.basic.service.MessageConsumer;
import com.legendshop.basic.service.MessageHandler;
import com.legendshop.basic.service.SysParamsService;
import com.legendshop.common.core.config.sys.params.MsgSendConfig;
import com.legendshop.common.rabbitmq.constants.AmqpConst;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 推送消息消费者
 *
 * @author legendshop
 */
@Component
@Slf4j
public class MessageConsumerImpl implements MessageConsumer {

	@Autowired
	private SysParamsService sysParamsService;

	@Autowired
	private Map<String, MessageHandler> messageHandlerMap;


	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.MESSAGE_QUEUE),
			exchange = @Exchange(value = AmqpConst.MESSAGE_EXCHANGE), key = {AmqpConst.MESSAGE_ROUTING_KEY}
	))
	public void consumeMessage(String msg, Channel channel, Message message) throws IOException {

		log.info("接收到消息：" + msg);
		log.info("消息体：" + new String(message.getBody()));
		try {
			boolean flag = JSONUtil.isJsonArray(msg);
			if (flag) {
				JSONArray jsonArray = JSONUtil.parseArray(msg);
				List<MsgSendDTO> msgSendDtoList = JSONUtil.toList(jsonArray, MsgSendDTO.class);
				msgSendDtoList.forEach(this::consumeMsg);
			} else {
				MsgSendDTO msgSendDTO = JSONUtil.toBean(msg, MsgSendDTO.class);
				consumeMsg(msgSendDTO);
			}
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (Exception e) {
			// 消息推送不要求重推
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			e.printStackTrace();
		}
	}

	@Override
	public void consumeMsg(MsgSendDTO msgSendDTO) {
		//根据消息类型的不同分开处理
		MsgSendConfig msgSendConfig = sysParamsService.getConfigDtoByParamName(msgSendDTO.getSysParamNameEnum().name(), MsgSendConfig.class);
		if (msgSendConfig == null) {
			log.info("站内信参数为空！{}", msgSendDTO.getSysParamNameEnum().name());
			return;
		}

		messageHandlerMap.forEach((k, messageHandler) -> {
			messageHandler.handleMessage(msgSendDTO, msgSendConfig);
		});
	}

}
