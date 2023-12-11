/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.mq;

import com.legendshop.common.rabbitmq.constants.AmqpConst;
import com.legendshop.product.constants.ProductConst;
import com.legendshop.product.service.ProductService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author legendshop
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class ProductRabbitListener {

	final ProductService productService;

	/**
	 * 预约商品到点上架
	 */
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.DELAY_APPOINT_ONLINE_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.DELAY_EXCHANGE,
					arguments = {
							@Argument(name = "x-delayed-type", value = "direct"),
							@Argument(name = "x-message-ttl", value = "1000", type = "java.lang.Long")
					}, delayed = Exchange.TRUE),
			key = AmqpConst.DELAY_APPOINT_ONLINE_ROUTING_KEY)
	)
	void appointProductOnline(String msg, Message message, Channel channel) {
		try {
			log.info("预约商品到点上架，接受到消息 Msg：{}", msg);
			if (null == msg) {
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				return;
			}
			this.productService.appointOnline(Long.parseLong(msg));
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 预售结束
	 *
	 * @param productId
	 * @param message
	 * @param channel
	 */
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = ProductConst.PRE_SELL_FINISH_QUEUE, durable = "true"),
			exchange = @Exchange(value = ProductConst.PRODUCT_EXCHANGE,
					arguments = {
							@Argument(name = "x-delayed-type", value = "direct"),
							@Argument(name = "x-message-ttl", value = "1000", type = "java.lang.Long")
					}, delayed = Exchange.TRUE),
			key = ProductConst.PRE_SELL_FINISH_ROUTING_KEY)
	)
	void preSellFinish(Long productId, Message message, Channel channel) {
		try {
			log.info("预约商品到点结束，接受到消息 Msg：{}", productId);
			if (null == productId) {
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				return;
			}
			productService.preSellFinish(productId);
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
