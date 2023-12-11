/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.mq;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.legendshop.basic.api.AmqpTaskApi;
import com.legendshop.basic.api.MessageApi;
import com.legendshop.basic.dto.AmqpTaskDTO;
import com.legendshop.basic.dto.MessagePushDTO;
import com.legendshop.basic.dto.MsgSendParamDTO;
import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import com.legendshop.basic.enums.MsgSendParamEnum;
import com.legendshop.basic.enums.MsgSendTypeEnum;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.rabbitmq.constants.AmqpConst;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.legendshop.order.dao.OrderDao;
import com.legendshop.order.dto.PreSellOrderDTO;
import com.legendshop.order.entity.Order;
import com.legendshop.order.service.PreSellOrderService;
import com.legendshop.product.enums.PreSellPayType;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author legendshop
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PreSellOrderRabbitListener {

	final OrderDao orderDao;
	final MessageApi messageApi;
	final AmqpTaskApi amqpTaskApi;
	final AmqpSendMsgUtil amqpSendMsgUtil;
	final PreSellOrderService preSellOrderService;

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.DELAY_CANCEL_UNPAID_PRE_SELL_ORDER_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.DELAY_EXCHANGE,
					arguments = {@Argument(name = "x-delayed-type", value = "direct")}, delayed = Exchange.TRUE),
			key = AmqpConst.DELAY_CANCEL_UNPAID_PRE_SELL_ORDER_ROUTING_KEY)
	)
	public void unpaidBalancePreSellOrder(Long orderId, Message message, Channel channel) throws IOException {
		log.info("开始处理预售尾款支付超时~");
		this.preSellOrderService.cancelOrderByIds(Collections.singletonList(orderId));
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
	}

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = AmqpConst.DELAY_PRE_SELL_ORDER_FINAL_PAYMENT_NOTICE_QUEUE, durable = "true"),
			exchange = @Exchange(value = AmqpConst.DELAY_EXCHANGE,
					arguments = {@Argument(name = "x-delayed-type", value = "direct")}, delayed = Exchange.TRUE),
			key = AmqpConst.DELAY_PRE_SELL_ORDER_FINAL_PAYMENT_NOTICE_ROUTING_KEY)
	)
	public void preSellOrderFinalPaymentNotice(Long orderId, Message message, Channel channel) throws IOException {
		log.info("开始发送预售尾款支付通知~, {}", orderId);
		PreSellOrderDTO presellOrderDTO = preSellOrderService.getByOrderId(orderId);
		if (null == presellOrderDTO) {
			log.info("预售订单为空，不需要处理~");
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			return;
		}

		Date finalMStart = presellOrderDTO.getFinalMStart();

		if (finalMStart.after(DateUtil.date())) {
			log.info("发送预售尾款支付处理消息队列！！！");

			AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
			amqpTaskDTO.setExchange(AmqpConst.DELAY_EXCHANGE);
			amqpTaskDTO.setRoutingKey(AmqpConst.DELAY_PRE_SELL_ORDER_FINAL_PAYMENT_NOTICE_ROUTING_KEY);
			amqpTaskDTO.setMessage(String.valueOf(presellOrderDTO.getOrderId()));
			amqpTaskDTO.setDelayTime(finalMStart);
			amqpTaskApi.convertAndSend(amqpTaskDTO);

			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			return;
		}

		if (PreSellPayType.FULL_AMOUNT.value().equals(presellOrderDTO.getPayPctType())) {
			log.info("预售订单为全款支付，不需要处理~");
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			return;
		}

		if (ObjectUtil.isEmpty(presellOrderDTO.getFinalMStart())) {
			log.info("预售订单尾款支付为空，不需要处理~");
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			return;
		}

		if (!presellOrderDTO.getPayDepositFlag()) {
			log.info("预售订单定金未支付，不需要处理~");
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			return;
		}

		if (presellOrderDTO.getPayFinalFlag()) {
			log.info("预售订单尾款已支付，不需要处理~");
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			return;
		}

		Order order = orderDao.getById(orderId);

		// 发布预售尾款支付站内信通知用户
		List<MsgSendParamDTO> msgSendParamDTOS = new ArrayList<>();
		//站内信替换参数内容
		MsgSendParamDTO productDTO = new MsgSendParamDTO(MsgSendParamEnum.PRODUCT_NAME, order.getProductName(), "black");
		msgSendParamDTOS.add(productDTO);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String target = sdf.format(presellOrderDTO.getPreSaleStart());

		//微信 first.DATA，可自定义
		MsgSendParamDTO first = new MsgSendParamDTO(MsgSendParamEnum.FIRST, "客官，可以付尾款啦", "black");
		MsgSendParamDTO KEYWORD1 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD1, orderId.toString(), "black");
		MsgSendParamDTO KEYWORD2 = new MsgSendParamDTO(MsgSendParamEnum.KEYWORD2, target.toString(), "black");

		//remark商家自定义
		MsgSendParamDTO REMARK = new MsgSendParamDTO(MsgSendParamEnum.REMARK, "尾款支付提醒", "black");
		msgSendParamDTOS.add(first);
		msgSendParamDTOS.add(KEYWORD1);
		msgSendParamDTOS.add(KEYWORD2);

		msgSendParamDTOS.add(REMARK);

		// 微信公众号发送通知提醒用户支付订单  模板参数替换内容
		List<MsgSendParamDTO> urlParamList = new ArrayList<>();
		MsgSendParamDTO url = new MsgSendParamDTO(MsgSendParamEnum.ORDER_ID, orderId.toString(), "#173177");
		urlParamList.add(url);

		messageApi.push(new MessagePushDTO()
				.setMsgReceiverTypeEnum(MsgReceiverTypeEnum.ORDINARY_USER)
				.setReceiveIdArr(new Long[]{order.getUserId()})
				.setMsgSendType(MsgSendTypeEnum.ORDER_TO_PAY)
				.setSysParamNameEnum(SysParamNameEnum.PRE_SELL_ORDER_FINAL_PAYMENT_TO_USER)
				.setMsgSendParamDTOList(msgSendParamDTOS)
				//跳转路径参数 http://xxxx?xx=xx&xx=xx
				.setUrlParamList(urlParamList)
				.setDetailId(order.getId())
		);

		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		log.info("预售尾款支付通知发送完成~, {}", orderId);
	}

}
