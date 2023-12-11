/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import com.legendshop.basic.enums.MsgSendTypeEnum;
import com.legendshop.basic.enums.SmsTemplateTypeEnum;
import com.legendshop.common.core.config.sys.params.MsgSendConfig;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author legendshop
 */
@Data
public class SmsSendParamDTO implements Serializable {

	private static final long serialVersionUID = -7164670323000828430L;

	/**
	 * 短信发送用户类型
	 */
	private String userType;

	/**
	 * 发送类型
	 */
	private MsgSendTypeEnum typeEnum;

	/**
	 * 短信发送具体模板
	 */
	private SmsTemplateTypeEnum smsTemplateTypeEnum;

	/**
	 * 短信发送具体参数
	 */
	private Map<String, String> templateParam;

	/**
	 * 发送手机号
	 */
	private List<String> phoneNumbers;

	/**
	 * 具体模板编码，模板文本
	 */
	private MsgSendConfig smsTemplateConfig;

	/**
	 * 请求ip
	 */
	private String ip;

}
