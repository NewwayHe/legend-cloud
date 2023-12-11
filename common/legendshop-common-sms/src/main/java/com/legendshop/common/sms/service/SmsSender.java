/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.sms.service;

import com.legendshop.common.sms.dto.SmsSendResponseDTO;
import com.legendshop.common.sms.dto.SmsTemplateDTO;

/**
 * 短信发送
 *
 * @author legendshop
 */
public interface SmsSender {

	SmsSendResponseDTO sendSms(SmsTemplateDTO smsTemplateDTO);
}
