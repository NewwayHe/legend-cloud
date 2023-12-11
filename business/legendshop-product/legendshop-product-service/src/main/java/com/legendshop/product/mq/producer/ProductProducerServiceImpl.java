/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.mq.producer;

import com.legendshop.basic.api.AmqpTaskApi;
import com.legendshop.basic.dto.AmqpTaskDTO;
import com.legendshop.common.rabbitmq.constants.AmqpConst;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.legendshop.product.constants.ProductConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Date;

/**
 * @author legendshop
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductProducerServiceImpl implements ProductProducerService {

	final AmqpTaskApi amqpTaskApi;
	final AmqpSendMsgUtil amqpSendMsgUtil;

	@Override
	public void appointOnLine(Long productId, Date time) {
		log.info("商品预约上架，发送延迟消息 id：{}, time：{}", productId, time);

		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			// 保证在事务提交后发送MQ
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public void afterCommit() {
					AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
					amqpTaskDTO.setExchange(AmqpConst.DELAY_EXCHANGE);
					amqpTaskDTO.setRoutingKey(AmqpConst.DELAY_APPOINT_ONLINE_ROUTING_KEY);
					amqpTaskDTO.setMessage(String.valueOf(productId));
					amqpTaskDTO.setDelayTime(time);
					amqpTaskApi.convertAndSend(amqpTaskDTO);
				}
			});
		} else {
			AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
			amqpTaskDTO.setExchange(AmqpConst.DELAY_EXCHANGE);
			amqpTaskDTO.setRoutingKey(AmqpConst.DELAY_APPOINT_ONLINE_ROUTING_KEY);
			amqpTaskDTO.setMessage(String.valueOf(productId));
			amqpTaskDTO.setDelayTime(time);
			amqpTaskApi.convertAndSend(amqpTaskDTO);
		}

//		long between = DateUtil.between(DateUtil.date(), time, DateUnit.SECOND, false);
//		long betweenDay = DateUtil.between(DateUtil.date(), time, DateUnit.DAY, false);
//		log.info("发送预约上架消息队列！！！");
//		//避免天数过长，如果超过三十天就先发送三十天的消息
//		if (betweenDay > 30) {
//			//30天接力
//			log.info("预约上架消息队列接力！！！");
//			DateTime month = DateUtil.offsetMonth(DateUtil.date(), 1);
//			between = DateUtil.between(DateUtil.date(), month, DateUnit.SECOND, false);
//		}
//		this.amqpSendMsgUtil.convertAndSend(AmqpConst.DELAY_EXCHANGE,
//				AmqpConst.DELAY_APPOINT_ONLINE_ROUTING_KEY, productId, between, ChronoUnit.SECONDS, true);
	}

	@Override
	public void preSellFinish(Long productId, Date time) {
		if (null == productId || time == null) {
			return;
		}

		AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
		amqpTaskDTO.setExchange(ProductConst.PRODUCT_EXCHANGE);
		amqpTaskDTO.setRoutingKey(ProductConst.PRE_SELL_FINISH_ROUTING_KEY);
		amqpTaskDTO.setMessage(String.valueOf(productId));
		amqpTaskDTO.setDelayTime(time);
		amqpTaskApi.convertAndSend(amqpTaskDTO);

//		long between = DateUtil.between(DateUtil.date(), time, DateUnit.SECOND, false);
//		long betweenDay = DateUtil.between(DateUtil.date(), time, DateUnit.DAY, false);
//		log.info("发送预售结束处理消息队列！！！");
//		//避免天数过长，如果超过三十天就先发送三十天的消息
//		if (betweenDay > 30) {
//			//30天接力
//			log.info("预售结束处理消息队列接力！！！");
//			DateTime month = DateUtil.offsetMonth(DateUtil.date(), 1);
//			between = DateUtil.between(DateUtil.date(), month, DateUnit.SECOND, false);
//		}
//		amqpSendMsgUtil.convertAndSend(ProductConst.PRODUCT_EXCHANGE,
//				ProductConst.PRE_SELL_FINISH_ROUTING_KEY, productId, between, ChronoUnit.SECONDS, true);
	}
}
