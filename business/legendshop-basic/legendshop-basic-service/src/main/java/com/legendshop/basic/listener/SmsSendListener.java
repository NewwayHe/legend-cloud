/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.listener;

import com.legendshop.basic.service.SmsSendService;
import com.legendshop.common.sms.event.SmsSendEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 短信发送
 *
 * @author legendshop
 */
@Component
@AllArgsConstructor
public class SmsSendListener {

	final SmsSendService smsSendService;

	/**
	 * 发送短信的方法
	 * 监听SmsSendEvent类
	 */
	@Async
	@Order
	@EventListener(SmsSendEvent.class)
	public void smsSend(SmsSendEvent smsSendEvent) {
		this.smsSendService.sendSmsAsync(smsSendEvent.getSmsTemplateDTO());
	}
}
