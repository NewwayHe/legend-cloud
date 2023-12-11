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
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author legendshop
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SmsTemplateDTO {

	private String signName;
	private String templateCode;
	private Map<String, String> templateParam;
	private List<String> phoneNumbers;
	private Map<String, Object> smsLogParam;

}
