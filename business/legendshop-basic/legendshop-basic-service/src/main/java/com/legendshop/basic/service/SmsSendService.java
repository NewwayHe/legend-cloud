/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import com.legendshop.basic.dto.SmsSendParamDTO;
import com.legendshop.common.core.constant.R;

/**
 * @author legendshop
 */
public interface SmsSendService {

	/**
	 * 发送短信异步事件
	 */
	R<String> sendSms(SmsSendParamDTO sendParamDTO);

	/**
	 *
	 */
	R<Void> sendSmsAsync(Object smsTemplateDTO);

}
