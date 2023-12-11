/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.mq;

import cn.hutool.json.JSONUtil;
import com.legendshop.data.constants.AmqpConst;
import com.legendshop.data.dto.DataUserPurchasingDTO;
import com.legendshop.data.entity.DataUserPurchasing;
import com.legendshop.data.service.DataService;
import com.legendshop.data.service.convert.DataUserPurchasingConverter;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户订单数据存储监听
 *
 * @author legendshop
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserOrderDataListener {

	@Autowired
	private DataService dataService;

	@Autowired
	private DataUserPurchasingConverter purchasingConverter;


	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.LEGENDSHOP_DATA_ORDER_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.LEGENDSHOP_DATA_EXCHANGE), key = {AmqpConst.LEGENDSHOP_DATA_ORDER_LOG_ROUTING_KEY}
	))
	@SneakyThrows
	public void searchListener(DataUserPurchasingDTO purchasingDTO, Message message, Channel channel) {
		log.info("进入用户购买力数据统计保存队列，参数: {}", JSONUtil.toJsonStr(purchasingDTO));
		try {
			DataUserPurchasing from = purchasingConverter.from(purchasingDTO);
			dataService.savePurchasingUserData(from);
		} catch (Exception e) {
			log.info(e.getMessage());
		} finally {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
	}
}
