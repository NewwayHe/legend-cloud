/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.log.listener;


import com.legendshop.common.core.dto.SystemLogDTO;
import com.legendshop.common.log.event.SystemLogEvent;
import com.legendshop.common.rabbitmq.constants.AmqpConst;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * 日志监听者
 *
 * @author legendshop
 */
@AllArgsConstructor
public class SystemLogListener {

	private final AmqpSendMsgUtil amqpSendMsgUtil;

	/**
	 * 保存日志的方法
	 * 监听SysLogEvent类
	 *
	 * @param event
	 */
	@Async
	@Order
	@EventListener(SystemLogEvent.class)
	public void saveSysLog(SystemLogEvent event) {
		SystemLogDTO systemLogDTO = event.getSystemLogDTO();
		//发送mq消息
		amqpSendMsgUtil.convertAndSend(AmqpConst.SYSTEM_LOG_EXCHANGE, AmqpConst.SYSTEM_LOG_ROUTING_KEY, systemLogDTO);

		//发送mq消息给用户服务写登录历史
		amqpSendMsgUtil.convertAndSend(AmqpConst.SYSTEM_LOG_EXCHANGE, AmqpConst.SYSTEM_LOG_LOGIN_ROUTING_KEY, systemLogDTO);

	}
}
