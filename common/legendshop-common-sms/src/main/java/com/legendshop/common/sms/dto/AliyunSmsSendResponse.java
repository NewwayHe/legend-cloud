/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.sms.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 阿里云短信 响应
 *
 * @author legendshop
 */
@Data
public class AliyunSmsSendResponse implements Serializable {
	private static final long serialVersionUID = 3027641529273160124L;

	private String RequestId;

	private String Message;

	private String Code;
}
