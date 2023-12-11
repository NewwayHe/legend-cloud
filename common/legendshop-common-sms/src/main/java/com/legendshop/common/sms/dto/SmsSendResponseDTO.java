/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import static com.legendshop.common.sms.properties.SmsProperties.SmsType;

/**
 * 统一短信响应(用作存日志)
 *
 * @author legendshop
 */
@Data
@AllArgsConstructor
public class SmsSendResponseDTO implements Serializable {

	private static final long serialVersionUID = 2176212998175033077L;

	/**
	 * 短信返回状态码
	 */
	private String resultCode;

	/**
	 * 短信发送时间
	 */
	private Date sendTime;

	/**
	 * 发送渠道 参考SmsType
	 */
	private SmsType channelType;

	public SmsSendResponseDTO() {
		this.resultCode = "OK";
		this.sendTime = new Date();
		this.channelType = SmsType.na;
	}

}
