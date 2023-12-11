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
import cn.hutool.json.JSONUtil;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.basic.service.LogisticsService;
import com.legendshop.basic.service.SysParamsService;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.logistics.kuaidi100.request.SubscribeParam;
import com.legendshop.common.logistics.kuaidi100.request.SubscribeParameters;
import com.legendshop.common.logistics.kuaidi100.request.SubscribeReq;
import com.legendshop.common.logistics.properties.LogisticsProperties;
import com.legendshop.common.logistics.service.SubscribeService;
import com.legendshop.common.rabbitmq.constants.AmqpConst;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author legendshop
 */
@Slf4j
@Service
public class LogisticsServiceImpl implements LogisticsService {


	@Autowired
	private SysParamsService sysParamsService;

	@Autowired
	private SubscribeService subscribeService;

	@Override
	public boolean poll(SubscribeReq subscribeReq) {
		subscribeService.subscribe(subscribeReq);
		return Boolean.TRUE;
	}

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.LOGISTIC_SUBSCRIBE_QUEUE),
			exchange = @Exchange(value = AmqpConst.LOGISTIC_EXCHANGE), key = {AmqpConst.LOGISTIC_SUBSCRIBE_ROUTING_KEY}
	))
	public void consumePoll(String msg, Channel channel, Message message) throws IOException {
		log.info("接收到消息：" + msg);
		log.info("消息体：" + new String(message.getBody()));
		try {
			boolean flag = JSONUtil.isJsonArray(msg);
			if (flag) {
				poll(buildSubscribeReq(msg, false));
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			}
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (Exception e) {
			// 消息推送不要求重推
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			e.printStackTrace();
		}

	}

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.REFUND_LOGISTIC_SUBSCRIBE_QUEUE),
			exchange = @Exchange(value = AmqpConst.LOGISTIC_EXCHANGE), key = {AmqpConst.REFUND_LOGISTIC_SUBSCRIBE_ROUTING_KEY}
	))
	public void refundConsumePoll(String msg, Channel channel, Message message) throws IOException {

		log.info("接收到消息：{}", msg);
		log.info("消息体：{}", new String(message.getBody()));
		try {
			boolean flag = JSONUtil.isJsonArray(msg);
			if (flag) {
				poll(buildSubscribeReq(msg, true));
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			}
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (Exception e) {
			// 消息推送不要求重推
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			e.printStackTrace();
		}

	}

	/**
	 * 构建订阅的请求
	 *
	 * @param msg
	 * @param isRefundPoll 是否退货物流订阅
	 * @return
	 */
	private SubscribeReq buildSubscribeReq(String msg, Boolean isRefundPoll) {
		LogisticsProperties logisticsProperties = sysParamsService.getConfigDtoByParamName(SysParamNameEnum.LOGISTICS.name(), LogisticsProperties.class);
		if (ObjectUtil.isNull(logisticsProperties)) {
			throw new BusinessException("物流配置失败");
		}
		List<String> stringList = JSONUtil.toList(JSONUtil.parseArray(msg), String.class);
		/*物流编码*/
		String company = stringList.get(0);
		/*物流编号*/
		String number = stringList.get(1);
		SubscribeReq subscribeReq = new SubscribeReq();
		SubscribeParam subscribeParam = new SubscribeParam();
		subscribeParam.setCompany(company);
		subscribeParam.setNumber(number);
		subscribeParam.setKey(logisticsProperties.getAppKey());
		SubscribeParameters subscribeParameters = new SubscribeParameters();
		subscribeParameters.setCallbackurl(isRefundPoll ? logisticsProperties.getRefundCallbackUrl() : logisticsProperties.getLogisticsCallbackUrl());
		subscribeParam.setParameters(subscribeParameters);
		subscribeReq.setSchema("json");
		subscribeReq.setParam(subscribeParam);
		return subscribeReq;
	}

}
