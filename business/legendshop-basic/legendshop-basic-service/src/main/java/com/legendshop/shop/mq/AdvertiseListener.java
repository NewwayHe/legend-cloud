/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.mq;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.legendshop.activity.enums.AdvertiseStatusEnum;
import com.legendshop.search.api.EsIndexApi;
import com.legendshop.shop.constants.AmqpConst;
import com.legendshop.shop.dao.AdvertiseDao;
import com.legendshop.shop.dto.AdvertiseCountDTO;
import com.legendshop.shop.entity.Advertise;
import com.legendshop.shop.service.AdvertiseCountService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author legendshop
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdvertiseListener {

	@Autowired
	private AdvertiseCountService countService;

	@Autowired
	private AdvertiseDao advertiseDao;

	final EsIndexApi esIndexApi;

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = com.legendshop.common.rabbitmq.constants.AmqpConst.LEGENDSHOP_SHOP_ADVERTISE_END_QUEUE, durable = "true"),
			exchange = @Exchange(value = com.legendshop.common.rabbitmq.constants.AmqpConst.DELAY_EXCHANGE,
					arguments = {@Argument(name = "x-delayed-type", value = "direct")}, delayed = Exchange.TRUE),
			key = com.legendshop.common.rabbitmq.constants.AmqpConst.LEGENDSHOP_SHOP_ADVERTISE_LOG_END_ROUTING_KEY)
	)
	@SneakyThrows
	public void viewAdvertiseListener(Long id, Message message, Channel channel) {
		//广告结束延时

		log.info(" 收到广告到点结束消息:广告id:" + id + ",当前时间：" + DateUtil.date() + "延迟时间：" + message.getMessageProperties().getReceivedDelay() + "秒");
		log.info("广告延时队列：{}", JSONUtil.toJsonStr(id));
		try {
			Advertise byId = advertiseDao.getById(id);
			long between = DateUtil.between(byId.getEndTime(), DateUtil.date(), DateUnit.SECOND, false);
			if (between < 0) {
				return;
			}

			if (AdvertiseStatusEnum.START.value().equals(byId.getStatus()) || AdvertiseStatusEnum.SUSPEND.value().equals(byId.getStatus())) {
				byId.setStatus(AdvertiseStatusEnum.END.value());
			}
			advertiseDao.update(byId);

		} finally {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}

	}

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = com.legendshop.common.rabbitmq.constants.AmqpConst.LEGENDSHOP_SHOP_ADVERTISE_START_QUEUE, durable = "true"),
			exchange = @Exchange(value = com.legendshop.common.rabbitmq.constants.AmqpConst.DELAY_EXCHANGE,
					arguments = {@Argument(name = "x-delayed-type", value = "direct")}, delayed = Exchange.TRUE),
			key = com.legendshop.common.rabbitmq.constants.AmqpConst.LEGENDSHOP_SHOP_ADVERTISE_LOG_START_ROUTING_KEY)
	)
	@SneakyThrows
	public void viewAdvertiseStartListener(Long id, Message message, Channel channel) {
		//广告开始延时

		log.info(" 收到广告到点开始消息:广告id:" + id + ",当前时间：" + DateUtil.date() + "延迟时间：" + message.getMessageProperties().getReceivedDelay() + "秒");
		log.info("广告延时队列：{}", JSONUtil.toJsonStr(id));
		try {
			Advertise byId = advertiseDao.getById(id);

			long start = DateUtil.between(byId.getStartTime(), DateUtil.date(), DateUnit.SECOND, false);
			if (start < 0) {
				return;
			}
			if (AdvertiseStatusEnum.WAIT.value().equals(byId.getStatus())) {
				byId.setStatus(AdvertiseStatusEnum.START.value());
			}

			advertiseDao.update(byId);

		} finally {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}

	}

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.LEGENDSHOP_SHOP_ADVERTISE_CLICK_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.LEGENDSHOP_SHOP_CLICK_EXCHANGE), key = {AmqpConst.LEGENDSHOP_SHOP_ADVERTISE_LOG_CLICK_ROUTING_KEY}
	))
	@SneakyThrows
	public void viewClickListener(AdvertiseCountDTO dto, Message message, Channel channel) {
		log.info("广告点击统计数据队列：{}", JSONUtil.toJsonStr(dto));
		try {
			countService.updateClick(dto);
		} catch (Exception e) {
			log.info(e.getMessage());
		} finally {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}

	}

	/**
	 * 广告投放统计数据队列
	 */
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.LEGENDSHOP_SHOP_ADVERTISE_PUT_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.LEGENDSHOP_SHOP_PUT_EXCHANGE), key = {AmqpConst.LEGENDSHOP_SHOP_ADVERTISE_PUT_LOG_ROUTING_KEY}
	))
	@SneakyThrows
	public void viewPutListener(AdvertiseCountDTO dto, Message message, Channel channel) {
		log.info("广告投放统计数据队列：{}", JSONUtil.toJsonStr(dto));
		try {
			countService.updatePut(dto);
		} catch (Exception e) {
			log.info(e.getMessage());
		} finally {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}

	}
}
