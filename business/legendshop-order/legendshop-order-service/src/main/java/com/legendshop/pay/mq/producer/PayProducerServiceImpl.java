/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.mq.producer;

import cn.hutool.core.util.ObjectUtil;
import com.legendshop.basic.api.AmqpTaskApi;
import com.legendshop.basic.dto.AmqpTaskDTO;
import com.legendshop.common.rabbitmq.constants.PayAmqpConst;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.legendshop.data.constants.AmqpConst;
import com.legendshop.data.dto.DataUserPurchasingDTO;
import com.legendshop.order.dto.OrderDTO;
import com.legendshop.order.dto.PreSellOrderDTO;
import com.legendshop.order.enums.OrderStatusEnum;
import com.legendshop.pay.constants.YeepayConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author legendshop
 */
@Slf4j
@Service
public class PayProducerServiceImpl implements PayProducerService {

	@Autowired
	private AmqpTaskApi amqpTaskApi;

	@Autowired
	private AmqpSendMsgUtil amqpSendMsgUtil;

	@Override
	public void updateBuys(String numberListJsonStr) {
		amqpSendMsgUtil.convertAndSend(PayAmqpConst.PAY_EXCHANGE, PayAmqpConst.UPDATE_BUYS_ROUTING_KEY, numberListJsonStr);
	}

	@Override
	public void sendPayMessage(OrderDTO orderDTO) {
		if (OrderStatusEnum.PRESALE_DEPOSIT.getValue().equals(orderDTO.getStatus())) {
			return;
		}

		DataUserPurchasingDTO dto = new DataUserPurchasingDTO();
		dto.setUserId(orderDTO.getUserId());
		dto.setQuantity(orderDTO.getProductQuantity());
		dto.setCreateTime(new Date());
		dto.setPayAmount(orderDTO.getActualTotalPrice());
		dto.setShopId(orderDTO.getShopId());
		dto.setOrderId(orderDTO.getId());
		amqpSendMsgUtil.convertAndSend(AmqpConst.LEGENDSHOP_DATA_EXCHANGE, AmqpConst.LEGENDSHOP_DATA_ORDER_LOG_ROUTING_KEY, dto);

	}

	@Override
	public void yeepayRegister(Long shopId) {
		amqpSendMsgUtil.convertAndSend(YeepayConstant.YEEPAY_EXCHANGE, YeepayConstant.YEEPAY_SHOP_INCOMING_REGISTER_ROUTING_KEY, shopId, true);
	}

	@Override
	public void handlePreSellFinalPaymentTimeOut(PreSellOrderDTO orderDTO) {
		Date finalMEnd = orderDTO.getFinalMEnd();
		if (ObjectUtil.isEmpty(finalMEnd)) {
			return;
		}

		AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
		amqpTaskDTO.setExchange(com.legendshop.common.rabbitmq.constants.AmqpConst.DELAY_EXCHANGE);
		amqpTaskDTO.setRoutingKey(com.legendshop.common.rabbitmq.constants.AmqpConst.DELAY_CANCEL_UNPAID_PRE_SELL_ORDER_ROUTING_KEY);
		amqpTaskDTO.setMessage(String.valueOf(orderDTO.getOrderId()));
		amqpTaskDTO.setDelayTime(finalMEnd);
		amqpTaskApi.convertAndSend(amqpTaskDTO);

//		DateTime now = DateUtil.date();
//		// 如果小于1天，则直接发队列，否则由定时任务去处理
//		if (DateUtil.between(now, finalMEnd, DateUnit.DAY, false) < 0) {
//			log.info("开始推送预售尾款支付超时队列~");
//			long delayTime = DateUtil.between(now, finalMEnd, DateUnit.SECOND, false);
//			this.amqpSendMsgUtil.convertAndSend(com.legendshop.common.rabbitmq.constants.AmqpConst.DELAY_EXCHANGE,
//					com.legendshop.common.rabbitmq.constants.AmqpConst.DELAY_CANCEL_UNPAID_PRE_SELL_ORDER_ROUTING_KEY, orderDTO.getOrderId(), delayTime, ChronoUnit.SECONDS);
//		}
	}

	@Override
	public void handlePreSellFinalPaymentNotice(PreSellOrderDTO orderDTO) {
		Date finalMStart = orderDTO.getFinalMStart();
		if (ObjectUtil.isEmpty(finalMStart)) {
			return;
		}

		AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
		amqpTaskDTO.setExchange(com.legendshop.common.rabbitmq.constants.AmqpConst.DELAY_EXCHANGE);
		amqpTaskDTO.setRoutingKey(com.legendshop.common.rabbitmq.constants.AmqpConst.DELAY_PRE_SELL_ORDER_FINAL_PAYMENT_NOTICE_ROUTING_KEY);
		amqpTaskDTO.setMessage(String.valueOf(orderDTO.getOrderId()));
		amqpTaskDTO.setDelayTime(finalMStart);
		amqpTaskApi.convertAndSend(amqpTaskDTO);

//		long between = DateUtil.between(DateUtil.date(), finalMStart, DateUnit.SECOND, false);
//		long betweenDay = DateUtil.between(DateUtil.date(), finalMStart, DateUnit.DAY, false);
//
//		if (DateUtil.date().after(finalMStart)) {
//			between = 0L;
//		} else {
//			log.info("发送预售尾款支付处理消息队列！！！");
//			//避免天数过长，如果超过三十天就先发送三十天的消息
//			if (betweenDay > 30) {
//				//30天接力
//				log.info("预售尾款支付处理消息队列接力！！！");
//				DateTime month = DateUtil.offsetMonth(DateUtil.date(), 1);
//				between = DateUtil.between(DateUtil.date(), month, DateUnit.SECOND, false);
//			}
//		}
//		this.amqpSendMsgUtil.convertAndSend(com.legendshop.common.rabbitmq.constants.AmqpConst.DELAY_EXCHANGE,
//				com.legendshop.common.rabbitmq.constants.AmqpConst.DELAY_PRE_SELL_ORDER_FINAL_PAYMENT_NOTICE_ROUTING_KEY, orderDTO.getOrderId(), between, ChronoUnit.SECONDS);
	}

}
