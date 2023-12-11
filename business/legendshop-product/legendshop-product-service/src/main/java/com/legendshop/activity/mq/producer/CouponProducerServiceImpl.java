/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.mq.producer;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.legendshop.activity.dao.CouponUserDao;
import com.legendshop.activity.dto.CouponDTO;
import com.legendshop.basic.api.AmqpTaskApi;
import com.legendshop.basic.dto.AmqpTaskDTO;
import com.legendshop.common.rabbitmq.constants.AmqpConst;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.legendshop.user.api.OrdinaryUserApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * @author legendshop
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponProducerServiceImpl implements CouponProducerService {

	final CouponUserDao couponUserDao;
	final AmqpTaskApi amqpTaskApi;
	final AmqpSendMsgUtil amqpSendMsgUtil;
	final OrdinaryUserApi ordinaryUserApi;

	@Override
	public void deleteCouponProduct(Long couponId) {
		log.info("优惠券，发送延迟消息 couponId：{}", couponId);
		this.amqpSendMsgUtil.convertAndSend(AmqpConst.DELAY_EXCHANGE,
				AmqpConst.COUPON_DELAY_DELETE_ROUTING_KEY, couponId, 15L, ChronoUnit.MINUTES);
	}

	@Override
	public void deleteCouponShop(Long couponId) {
		log.info("优惠券，发送延迟消息 couponId：{}", couponId);
		this.amqpSendMsgUtil.convertAndSend(AmqpConst.DELAY_EXCHANGE,
				AmqpConst.COUPON_DELAY_DELETE_PLATFORM_ROUTING_KEY, couponId, 15L, ChronoUnit.MINUTES);
	}

	@Override
	public void couponOnLine(CouponDTO couponDTO) {

		String objectJson = JSONUtil.parseObj(couponDTO).toString();
		log.info("优惠券：发送到时开始领取延迟消息 id：{}, time：{}", objectJson, couponDTO.getReceiveStartTime());

		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			// 保证在事务提交后发送MQ
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public void afterCommit() {
					AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
					amqpTaskDTO.setExchange(AmqpConst.DELAY_EXCHANGE);
					amqpTaskDTO.setRoutingKey(AmqpConst.DELAY_COUPON_ONLINE_ROUTING_KEY);
					amqpTaskDTO.setMessage(objectJson);
					amqpTaskDTO.setDelayTime(couponDTO.getReceiveStartTime());
					amqpTaskApi.convertAndSend(amqpTaskDTO);
				}
			});
		} else {
			AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
			amqpTaskDTO.setExchange(AmqpConst.DELAY_EXCHANGE);
			amqpTaskDTO.setRoutingKey(AmqpConst.DELAY_COUPON_ONLINE_ROUTING_KEY);
			amqpTaskDTO.setMessage(objectJson);
			amqpTaskDTO.setDelayTime(couponDTO.getReceiveStartTime());
			amqpTaskApi.convertAndSend(amqpTaskDTO);
		}

	}

	@Override
	public void couponOffLine(Long id, Date offTime) {
		log.info("优惠券：发送到时终止领取延迟消息 id：{}, offTime：{}", id, offTime);
		/*如果活动结束时间早于当前时间，1s后立即结束活动*/
//		long seconds = getSeconds(offTime, new Date(), 1);
//		this.amqpSendMsgUtil.convertAndSend(AmqpConst.DELAY_EXCHANGE,
//				AmqpConst.DELAY_COUPON_OFFLINE_ROUTING_KEY, id, seconds, ChronoUnit.SECONDS, true);

		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			// 保证在事务提交后发送MQ
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public void afterCommit() {
					AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
					amqpTaskDTO.setExchange(AmqpConst.DELAY_EXCHANGE);
					amqpTaskDTO.setRoutingKey(AmqpConst.DELAY_COUPON_OFFLINE_ROUTING_KEY);
					amqpTaskDTO.setMessage(id.toString());
					amqpTaskDTO.setDelayTime(offTime);
					amqpTaskApi.convertAndSend(amqpTaskDTO);
				}
			});
		} else {
			AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
			amqpTaskDTO.setExchange(AmqpConst.DELAY_EXCHANGE);
			amqpTaskDTO.setRoutingKey(AmqpConst.DELAY_COUPON_OFFLINE_ROUTING_KEY);
			amqpTaskDTO.setMessage(id.toString());
			amqpTaskDTO.setDelayTime(offTime);
			amqpTaskApi.convertAndSend(amqpTaskDTO);
		}
	}

	@Override
	public void userCouponOnLine(List<Long> ids, Date onTime) {
		log.info("用户优惠券：发送到时生效延迟消息 id：{}, onTime：{}", ids.toString(), onTime);

		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			// 保证在事务提交后发送MQ
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public void afterCommit() {
					AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
					amqpTaskDTO.setExchange(AmqpConst.DELAY_EXCHANGE);
					amqpTaskDTO.setRoutingKey(AmqpConst.DELAY_USER_COUPON_ONLINE_ROUTING_KEY);
					amqpTaskDTO.setMessage(JSONUtil.toJsonStr(ids));
					amqpTaskDTO.setDelayTime(onTime);
					amqpTaskApi.convertAndSend(amqpTaskDTO);
				}
			});
		} else {
			AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
			amqpTaskDTO.setExchange(AmqpConst.DELAY_EXCHANGE);
			amqpTaskDTO.setRoutingKey(AmqpConst.DELAY_USER_COUPON_ONLINE_ROUTING_KEY);
			amqpTaskDTO.setMessage(JSONUtil.toJsonStr(ids));
			amqpTaskDTO.setDelayTime(onTime);
			amqpTaskApi.convertAndSend(amqpTaskDTO);
		}

	}

	@Override
	public void userCouponOffLine(List<Long> ids, Date offTime) {
		if (ObjectUtil.isEmpty(ids)) {
			log.info("用户优惠券：发送到时失效延迟消息， ID为空");
			return;
		}
		log.info("用户优惠券：发送到时失效延迟消息 id：{}, time：{}", ids.toString(), offTime);

		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			// 保证在事务提交后发送MQ
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public void afterCommit() {
					AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
					amqpTaskDTO.setExchange(AmqpConst.DELAY_EXCHANGE);
					amqpTaskDTO.setRoutingKey(AmqpConst.DELAY_USER_COUPON_OFFLINE_ROUTING_KEY);
					amqpTaskDTO.setMessage(JSONUtil.toJsonStr(ids));
					amqpTaskDTO.setDelayTime(offTime);
					amqpTaskApi.convertAndSend(amqpTaskDTO);
				}
			});
		} else {
			AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
			amqpTaskDTO.setExchange(AmqpConst.DELAY_EXCHANGE);
			amqpTaskDTO.setRoutingKey(AmqpConst.DELAY_USER_COUPON_OFFLINE_ROUTING_KEY);
			amqpTaskDTO.setMessage(JSONUtil.toJsonStr(ids));
			amqpTaskDTO.setDelayTime(offTime);
			amqpTaskApi.convertAndSend(amqpTaskDTO);
		}

	}

	private long getSeconds(Date time, Date date, int i) {
		if (time.before(date)) {
			return i;
		} else {
			long day = DateUtil.between(date, time, DateUnit.DAY);
			if (day > 30) {
				DateTime month = DateUtil.offsetMonth(DateUtil.date(), 1);
				return DateUtil.between(DateUtil.date(), month, DateUnit.SECOND, false);
			}
			return DateUtil.between(date, time, DateUnit.SECOND);
		}
	}
}
