/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.mq;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.legendshop.activity.dao.CouponDao;
import com.legendshop.activity.dao.CouponProductDao;
import com.legendshop.activity.dao.CouponShopDao;
import com.legendshop.activity.dao.CouponUserDao;
import com.legendshop.activity.dto.CouponDTO;
import com.legendshop.activity.dto.CouponUserDTO;
import com.legendshop.activity.entity.Coupon;
import com.legendshop.activity.entity.CouponUser;
import com.legendshop.activity.enums.*;
import com.legendshop.activity.mq.producer.CouponProducerService;
import com.legendshop.activity.service.CouponProductService;
import com.legendshop.activity.service.CouponService;
import com.legendshop.activity.service.CouponUserService;
import com.legendshop.common.rabbitmq.constants.AmqpConst;
import com.legendshop.common.util.RandomUtil;
import com.legendshop.search.api.EsIndexApi;
import com.legendshop.user.api.OrdinaryUserApi;
import com.legendshop.user.dto.OrdinaryUserDTO;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author legendshop
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CouponRabbitListener {

	final CouponDao couponDao;
	final EsIndexApi esIndexApi;
	final CouponProductDao couponProductDao;
	final CouponUserDao couponUserDao;
	final CouponService couponService;
	final CouponUserService couponUserService;
	final CouponProductService couponProductService;
	final OrdinaryUserApi ordinaryUserApi;
	final CouponProducerService couponProducerService;
	final CouponShopDao couponShopDao;
	final CouponProducerService producerService;

	/**
	 * 【优惠券】未保存优惠券的优惠券商品超时自动删除
	 *
	 * @param couponId 优惠券id
	 * @param message
	 * @param channel
	 */
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.COUPON_DELAY_DELETE_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.DELAY_EXCHANGE, type = ExchangeTypes.DIRECT,
					arguments = @Argument(name = "x-delayed-type", value = "direct"), delayed = Exchange.TRUE),
			key = AmqpConst.COUPON_DELAY_DELETE_ROUTING_KEY))
	public void cancelRefundDelay(Long couponId, Message message, Channel channel) throws IOException {
		log.info("收到自动取消优惠券消息:优惠券id:" + couponId + ",当前时间：" + DateUtil.date() + "延迟时间：" + message.getMessageProperties().getReceivedDelay() + "秒");
		Coupon coupon = couponDao.getById(couponId);
		/*如果没有查询到这条优惠券记录，就把这个优惠券的商品删除掉*/
		if (ObjectUtil.isEmpty(coupon)) {
			couponProductDao.deleteByCouponId(couponId);
		}
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		log.info("超时自动删除优惠券商品成功");
	}

	/**
	 * 【优惠券】未保存优惠券的优惠券商品超时自动删除
	 *
	 * @param couponId 优惠券id
	 * @param message
	 * @param channel
	 */
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.COUPON_DELAY_DELETE_PLATFORM_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.DELAY_EXCHANGE, type = ExchangeTypes.DIRECT,
					arguments = @Argument(name = "x-delayed-type", value = "direct"), delayed = Exchange.TRUE),
			key = AmqpConst.COUPON_DELAY_DELETE_PLATFORM_ROUTING_KEY))
	public void cancelRefundDelayPlatform(Long couponId, Message message, Channel channel) throws IOException {
		log.info("收到自动取消平台优惠券消息: 优惠券id:" + couponId + ",当前时间：" + DateUtil.date() + "延迟时间：" + message.getMessageProperties().getReceivedDelay() + "秒");
		Coupon coupon = couponDao.getById(couponId);
		/*如果没有查询到这条优惠券记录，就把这个优惠券的店铺删除掉*/
		if (ObjectUtil.isEmpty(coupon) || CouponUseTypeEnum.GENERAL.getValue().equals(coupon.getUseType())) {
			couponShopDao.deleteByCouponId(couponId);
		}
		channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
		log.info("超时自动删除优惠券店铺成功");
	}


	/**
	 * 【优惠券】到点生效
	 *
	 * @param msg
	 * @param message
	 * @param channel
	 */
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.DELAY_COUPON_ONLINE_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.DELAY_EXCHANGE,
					arguments = {
							@Argument(name = "x-delayed-type", value = "direct"),
							@Argument(name = "x-message-ttl", value = "1000", type = "java.lang.Long")
					}, delayed = Exchange.TRUE),
			key = AmqpConst.DELAY_COUPON_ONLINE_ROUTING_KEY)
	)
	public void couponDelayOnLine(String msg, Message message, Channel channel) {
		try {
			log.info("优惠券：接收到点活动开始消息 Msg：{}", msg);
			if (!JSONUtil.isJsonObj(msg)) {
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				return;
			}
			CouponDTO originCoupon = JSONUtil.toBean(msg, CouponDTO.class);
			CouponDTO couponDTO = couponService.getById(originCoupon.getId());
			if (null == couponDTO) {
				log.info("优惠券：找不到当前优惠券，活动到点开始队列结束，优惠券ID：{}", originCoupon.getId());
				return;
			}

			DateTime now = DateUtil.date();
			if (now.before(couponDTO.getReceiveStartTime())) {
				log.info("优惠券：接收到点活动开始接力");
				couponProducerService.couponOnLine(couponDTO);
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				return;
			}

			if (CouponStatusEnum.NOT_STARTED.getValue().equals(couponDTO.getStatus())) {
				/*优惠券活动生效时间*/
				Date onTime = ObjectUtil.isNotEmpty(couponDTO.getUseStartTime()) ? couponDTO.getUseStartTime() : DateUtil.offsetDay(now, couponDTO.getUseDayLater());
				/*优惠券活动失效时间*/
				Date offTime = ObjectUtil.isNotEmpty(couponDTO.getUseEndTime()) ? couponDTO.getUseEndTime() : DateUtil.offsetDay(onTime, couponDTO.getWithinDay());

				List<Long> idList = null;

				/*如果是对指定的手机号发放，优惠券活动开始的同时，直接发放优惠券到用户手中*/
				if (CouponDesignateEnum.MOBILE.getValue().equals(couponDTO.getDesignatedUser())) {
					List<OrdinaryUserDTO> ordinaryUserList = ordinaryUserApi.queryByMobile(originCoupon.getMobile()).getData();
					List<CouponUser> couponUserList = ordinaryUserList.stream().map(ordinaryUserDTO -> {
						CouponUser couponUser = new CouponUser();
						couponUser.setUserId(ordinaryUserDTO.getId());
						couponUser.setCouponId(couponDTO.getId());
						couponUser.setCouponTitle(couponDTO.getTitle());
						couponUser.setGetTime(couponDTO.getReceiveStartTime());
						couponUser.setGetType(CouponReceiveTypeEnum.TARGET_USER.getValue());
						couponUser.setStatus(CouponUserStatusEnum.UNUSED.getValue());
						if (now.before(onTime)) {
							couponUser.setStatus(CouponUserStatusEnum.NOT_STARTED.getValue());
						}
						couponUser.setCouponCode(RandomUtil.getRandomSn());
						couponUser.setUseStartTime(onTime);
						couponUser.setUseEndTime(offTime);
						couponUser.setPaymentAllAfterSalesRefundableFlag(couponDTO.getPaymentAllAfterSalesRefundableFlag());
						couponUser.setPaymentRefundableFlag(couponDTO.getPaymentRefundableFlag());
						couponUser.setNonPaymentRefundableFlag(couponDTO.getNonPaymentRefundableFlag());
						return couponUser;
					}).collect(Collectors.toList());
					couponDTO.setReceiveCount(couponUserList.size());
					idList = couponUserDao.save(couponUserList);

					if (now.before(offTime)) {
						couponProducerService.userCouponOnLine(idList, onTime);
					} else {
						couponProducerService.userCouponOffLine(idList, offTime);
					}
				}

				/*如果是卡密兑换，优惠券活动开始的同时，生成卡密优惠券【用户信息为空，用户兑换时补全】*/
				if (CouponReceiveTypeEnum.PWD.getValue().equals(couponDTO.getReceiveType())) {
					List<CouponUser> couponUserList = new ArrayList<>();
					for (int i = 0; i < couponDTO.getCount(); i++) {
						CouponUser couponUser = new CouponUser();
						couponUser.setCouponId(couponDTO.getId());
						couponUser.setCouponTitle(couponDTO.getTitle());
						couponUser.setCouponCode(RandomUtil.getRandomSn());
						couponUser.setId(couponUserDao.createId());
						couponUser.setPassword(couponUser.getId() + RandomUtil.getRandomString(4));
						couponUser.setGetTime(couponDTO.getReceiveStartTime());
						couponUser.setGetType(CouponReceiveTypeEnum.PWD.getValue());
						couponUser.setStatus(CouponUserStatusEnum.UNUSED.getValue());
						/*如果优惠券有指定使用时间段，则设置进去*/
						if (ObjectUtil.isNotEmpty(couponDTO.getUseStartTime())) {
							couponUser.setUseStartTime(couponDTO.getUseStartTime());
						}
						if (ObjectUtil.isNotEmpty(couponDTO.getUseEndTime())) {
							couponUser.setUseEndTime(couponDTO.getUseEndTime());
						}
						if (couponDTO.getUseDayLater() > 0) {
							couponUser.setStatus(CouponUserStatusEnum.NOT_STARTED.getValue());
						}
						couponUser.setPaymentAllAfterSalesRefundableFlag(couponDTO.getPaymentAllAfterSalesRefundableFlag());
						couponUser.setPaymentRefundableFlag(couponDTO.getPaymentRefundableFlag());
						couponUser.setNonPaymentRefundableFlag(couponDTO.getNonPaymentRefundableFlag());
						couponUserList.add(couponUser);
					}
					idList = couponUserDao.saveWithId(couponUserList);
					/*如果优惠券有指定使用时间段，则设置优惠券生效、失效队列*/
					if (now.before(offTime)) {
						couponProducerService.userCouponOnLine(idList, onTime);
					} else {
						couponProducerService.userCouponOffLine(idList, offTime);
					}
				}
				couponService.updateStatus(originCoupon.getId(), CouponStatusEnum.CONTAINS.getValue());

				// 优惠券活动失效
				couponProducerService.couponOffLine(couponDTO.getId(), couponDTO.getReceiveEndTime());

				// 创建优惠券索引，平台券不处理
				if (couponDTO.getShopProviderFlag()) {
					esIndexApi.createCouponIndexByCouponId(couponDTO.getId());
				}
			}
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (IOException e) {
			e.printStackTrace();
			try {
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 【优惠券】到点失效
	 *
	 * @param msg
	 * @param message
	 * @param channel
	 */
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.DELAY_COUPON_OFFLINE_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.DELAY_EXCHANGE,
					arguments = {
							@Argument(name = "x-delayed-type", value = "direct"),
							@Argument(name = "x-message-ttl", value = "1000", type = "java.lang.Long")
					}, delayed = Exchange.TRUE),
			key = AmqpConst.DELAY_COUPON_OFFLINE_ROUTING_KEY)
	)
	public void couponDelayOffLine(String msg, Message message, Channel channel) {
		try {
			log.info("优惠券：接收到点活动结束消息 Msg：{}", msg);
			if (null == msg) {
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				return;
			}
			CouponDTO couponDTO = couponService.getById(Long.parseLong(msg));
			if (null == couponDTO) {
				log.info("优惠券：找不到当前优惠券，活动到点结束队列结束，优惠券ID：{}", msg);
				return;
			}

			DateTime now = DateUtil.date();
			if (now.before(couponDTO.getReceiveEndTime())) {
				log.info("优惠券：接收到点活动结束接力");
				couponProducerService.couponOffLine(couponDTO.getId(), couponDTO.getReceiveEndTime());
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				return;
			}
			if (ObjectUtil.isNotEmpty(couponDTO) && (CouponStatusEnum.CONTAINS.getValue().equals(couponDTO.getStatus()) || CouponStatusEnum.PAUSE.getValue().equals(couponDTO.getStatus()))) {
				couponService.updateStatus(couponDTO.getId(), CouponStatusEnum.FINISHED.getValue());

				// 删除优惠券索引
				esIndexApi.deleteCouponIndexByCouponId(couponDTO.getId());
			}
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 【用户优惠券】到点生效
	 *
	 * @param msg
	 * @param message
	 * @param channel
	 */
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.DELAY_USER_COUPON_ONLINE_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.DELAY_EXCHANGE,
					arguments = {
							@Argument(name = "x-delayed-type", value = "direct")
					}, delayed = Exchange.TRUE),
			key = AmqpConst.DELAY_USER_COUPON_ONLINE_ROUTING_KEY)
	)
	public void userCouponValid(String msg, Message message, Channel channel) {
		try {
			log.info("用户优惠券：接收到点生效消息 Msg：{}", msg);
			List<Long> ids = JSONUtil.toList(JSONUtil.parseArray(msg), Long.class);
			if (null == msg) {
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				return;
			}
			List<CouponUserDTO> couponProducts = couponUserService.queryById(ids);
			if (CollUtil.isEmpty(couponProducts)) {
				log.info("用户优惠券列表查询为空 Msg：{}", msg);
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				return;
			}
			DateTime now = DateUtil.date();
			CouponUserDTO userDTO = couponProducts.get(0);
			if (now.before(userDTO.getUseStartTime())) {
				log.info("用户优惠券：接收到点生效接力");
				couponProducerService.userCouponOnLine(ids, userDTO.getUseStartTime());
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				return;
			}
			/*获取所有未生效的优惠券*/
			List<Long> notStartedIds = couponProducts.stream()
					.filter(couponUserDTO -> CouponUserStatusEnum.NOT_STARTED.getValue().equals(couponUserDTO.getStatus()))
					.map(CouponUserDTO::getId)
					.collect(Collectors.toList());
			/*更新所有未生效的优惠券为未使用*/
			if (CollUtil.isNotEmpty(notStartedIds)) {
				couponProductService.updateStatus(notStartedIds, CouponUserStatusEnum.UNUSED.getValue(), CouponUserStatusEnum.NOT_STARTED.getValue());
			}

			// 发送到期下线用户优惠券
			producerService.userCouponOffLine(ids, userDTO.getUseEndTime());
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 【用户优惠券】到点失效
	 *
	 * @param msg
	 * @param message
	 * @param channel
	 */
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.DELAY_USER_COUPON_OFFLINE_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.DELAY_EXCHANGE,
					arguments = {
							@Argument(name = "x-delayed-type", value = "direct"),
							@Argument(name = "x-message-ttl", value = "1000", type = "java.lang.Long")
					}, delayed = Exchange.TRUE),
			key = AmqpConst.DELAY_USER_COUPON_OFFLINE_ROUTING_KEY)
	)
	public void userCouponInvalid(String msg, Message message, Channel channel) {
		try {
			log.info("用户优惠券：接收到点失效消息 Msg：{}", msg);
			List<Long> ids = JSONUtil.toList(JSONUtil.parseArray(msg), Long.class);
			if (null == msg) {
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
				return;
			}
			List<CouponUserDTO> couponProducts = couponUserService.queryById(ids);
			if (CollUtil.isEmpty(couponProducts)) {
				log.info("用户优惠券列表查询为空 Msg：{}", msg);
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				return;
			}

			DateTime now = DateUtil.date();
			CouponUserDTO userDTO = couponProducts.get(0);
			if (now.before(userDTO.getUseEndTime())) {
				log.info("用户优惠券：接收到点失效接力");
				couponProducerService.userCouponOnLine(ids, userDTO.getUseEndTime());
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				return;
			}
			/*获取所有未使用的优惠券*/
			List<Long> unuseIds = couponProducts.stream()
					.filter(couponUserDTO -> !CouponUserStatusEnum.USED.getValue().equals(couponUserDTO.getStatus()))
					.map(CouponUserDTO::getId)
					.collect(Collectors.toList());
			/*更新所有未使用的优惠券为已失效*/
			if (CollUtil.isNotEmpty(unuseIds)) {
				couponProductService.updateInvalidStatus(unuseIds);
			}
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
