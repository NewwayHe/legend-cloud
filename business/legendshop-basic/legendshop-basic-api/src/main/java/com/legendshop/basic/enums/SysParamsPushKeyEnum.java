/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;

import lombok.Getter;

/**
 * 消息推送子项的key
 * 用于自动生成推送子项信息
 *
 * @author legendshop
 */
@Getter
public enum SysParamsPushKeyEnum {
	/**
	 * 短信模板code
	 */
	smsTemplateCode("smsTemplateCode", "短信模板code", "", "String"),

	/**
	 * 短信内容
	 */
	smsContent("smsContent", "短信内容", "", "String"),

	/**
	 * 短信开关
	 */
	smsEnabled("smsEnabled", "短信开关", "false", "Boolean"),

	/**
	 * 微信内容模板id
	 */
	wxTemplateId("wxTemplateId", "微信内容模板id", "", "String"),

	/**
	 * 微信内容
	 */
	wxContent("wxContent", "微信内容", "", "String"),

	/**
	 * 微信开关
	 */
	wxEnabled("wxEnabled", "微信开关", "false", "Boolean"),

	/**
	 * 短信内容
	 */
	systemMsgContent("systemMsgContent", "站内信内容", "", "String"),

	/**
	 * 短信开关
	 */
	systemMsgEnabled("systemMsgEnabled", "站内信开关", "false", "Boolean"),
	;

	private String keyWord;

	private String desc;

	private String value;

	private String dateType;

	SysParamsPushKeyEnum(String keyWord, String desc, String value, String dateType) {
		this.keyWord = keyWord;
		this.desc = desc;
		this.value = value;
		this.dateType = dateType;
	}

	SysParamsPushKeyEnum(String keyWord) {
		this.keyWord = keyWord;
	}
}
