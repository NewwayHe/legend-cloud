/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.rabbitmq.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * amqp发送工具类
 *
 * @author legendshop
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AmqpSendMsgUtil {

	private final AmqpTemplate amqpTemplate;


	/**
	 * 发送mq消息
	 *
	 * @param exchange   交换机
	 * @param routingKey 路由键
	 * @param message    消息实体
	 */
	public <T> void convertAndSend(String exchange, String routingKey, T message) {
		convertAndSend(exchange, routingKey, message, false);
	}

	/**
	 * 发送延迟队列消息
	 *
	 * @param exchange   交换机
	 * @param routingKey 路由键
	 * @param msg        消息实体
	 * @param delayTime  延迟时间  10
	 * @param unit       ChronoUnit.MINUTES 延迟单位(时，分，秒，天等)
	 */
	public <T> void convertAndSend(String exchange, String routingKey, T msg, Long delayTime, ChronoUnit unit) {
		convertAndSend(exchange, routingKey, msg, delayTime, unit, false);
	}


	/**
	 * 发送mq消息
	 *
	 * @param exchange    交换机
	 * @param routingKey  路由键
	 * @param message     消息实体
	 * @param afterCommit 是否需要事务后提交MQ(为true时，则该方法需要在事务内调用)
	 */
	public <T> void convertAndSend(String exchange, String routingKey, T message, boolean afterCommit) {
		try {
			if (afterCommit && TransactionSynchronizationManager.isSynchronizationActive()) {
				// 保证在事务提交后发送MQ
				log.info("发现本地事务，当前队列事务后提交，{}，{}, 时间戳：{}", exchange, routingKey, System.currentTimeMillis());
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						log.info("事务提交成功，发起队列，{}，{}, 时间戳：{}", exchange, routingKey, System.currentTimeMillis());
						amqpTemplate.convertAndSend(exchange, routingKey, message);
						log.info("事务提交成功，队列发起成功，{}，{}, 时间戳：{}", exchange, routingKey, System.currentTimeMillis());
					}
				});
			} else {
				log.info("没有本地事务，当前队列立刻提交，{}，{}, 时间戳：{}", exchange, routingKey, System.currentTimeMillis());
				amqpTemplate.convertAndSend(exchange, routingKey, message);
			}
			log.info("交换机:" + exchange + ",路由:" + routingKey + ",发送mq消息成功。");
		} catch (AmqpException e) {
			e.printStackTrace();
			log.error("发送消息失败,routingKey={},message={}", routingKey, message);
		}
	}

	/**
	 * 发送延迟队列消息
	 *
	 * @param exchange    交换机
	 * @param routingKey  路由键
	 * @param msg         消息实体
	 * @param delayTime   延迟时间  10
	 * @param unit        ChronoUnit.MINUTES 延迟单位(时，分，秒，天等)
	 * @param afterCommit 是否需要事务后提交MQ(为true时，则该方法需要在事务内调用)
	 */
	public <T> void convertAndSend(String exchange, String routingKey, T msg, Long delayTime, ChronoUnit unit, boolean afterCommit) {
		Duration duration = Duration.of(delayTime, unit);
		try {
			log.info("发送延迟队列消息{}", routingKey);
			MessagePostProcessor messagePostProcessor = getMessagePostProcessor(duration);
			if (afterCommit && TransactionSynchronizationManager.isSynchronizationActive()) {
				// 保证在事务提交后发送MQ
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						amqpTemplate.convertAndSend(exchange, routingKey, msg, messagePostProcessor);
					}
				});
			} else {
				amqpTemplate.convertAndSend(exchange, routingKey, msg, messagePostProcessor);
			}
			log.info("交换机:" + exchange + ",路由:" + routingKey + ",发送延迟队列消息成功。");
		} catch (AmqpException e) {
			e.printStackTrace();
			log.error("发送消息失败,routingKey={},message={}", routingKey, msg);
		}
	}


	/**
	 * 获取消息处理器
	 *
	 * @param duration
	 * @return
	 */
	private MessagePostProcessor getMessagePostProcessor(Duration duration) {
		return message -> {
			long millis = duration.toMillis();
			if (millis > 0xffffffffL) {
				throw new IllegalArgumentException("超时过长, 只支持 小于 4294967296 毫秒的延时值");
			}
			if (millis >= 0) {
				message.getMessageProperties().getHeaders().put("x-delay", millis);
			} else {
				message.getMessageProperties().getHeaders().remove("x-delay");
			}
			return message;
		};
	}


}
