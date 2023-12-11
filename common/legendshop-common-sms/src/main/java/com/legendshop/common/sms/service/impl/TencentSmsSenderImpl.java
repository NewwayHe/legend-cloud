/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.sms.service.impl;

import com.github.qcloudsms.SmsSingleSender;
import com.legendshop.common.sms.dto.SmsSendResponseDTO;
import com.legendshop.common.sms.dto.SmsTemplateDTO;
import com.legendshop.common.sms.properties.SmsProperties;
import com.legendshop.common.sms.service.SmsSender;

/**
 * @author legendshop
 */
public class TencentSmsSenderImpl implements SmsSender {


	private SmsSingleSender client;
	private SmsProperties properties;

	public TencentSmsSenderImpl(SmsProperties smsProperties) {
		this.properties = smsProperties;
	}

	@Override
	public SmsSendResponseDTO sendSms(SmsTemplateDTO smsTemplateDTO) {
		// TODO 暂未实现
		System.out.println("使用腾讯发送短信验证码");
		return null;
	}
}
