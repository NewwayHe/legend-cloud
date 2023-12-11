/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.rabbitmq.producer.impl;

import cn.hutool.json.JSONUtil;
import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.common.rabbitmq.constants.AmqpConst;
import com.legendshop.common.rabbitmq.producer.LogisticsProducerService;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author legendshop
 */
@Service
public class LogisticsProducerServiceImpl implements LogisticsProducerService {

	@Autowired
	private AmqpSendMsgUtil amqpSendMsgUtil;

	@Override
	public String poll(String company, String number) {
		List<String> str = new ArrayList<>();
		str.add(company);
		str.add(number);
		amqpSendMsgUtil.convertAndSend(AmqpConst.LOGISTIC_EXCHANGE, AmqpConst.LOGISTIC_SUBSCRIBE_ROUTING_KEY,
				JSONUtil.toJsonStr(str));
		return CommonConstants.OK;
	}

	@Override
	public String refundPoll(String company, String number) {
		List<String> str = new ArrayList<>();
		str.add(company);
		str.add(number);
		amqpSendMsgUtil.convertAndSend(AmqpConst.LOGISTIC_EXCHANGE, AmqpConst.REFUND_LOGISTIC_SUBSCRIBE_ROUTING_KEY,
				JSONUtil.toJsonStr(str));
		return CommonConstants.OK;
	}
}
