/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.config.sys.params;

import lombok.Data;

/**
 * 消息推送配置
 *
 * @author legendshop
 */
@Data
public class MsgSendConfig implements ParamsConfig {

	private static final long serialVersionUID = -4016856690162528034L;

	/**
	 * 短信模板
	 */
	private String smsTemplateCode;

	/**
	 * 短信内容
	 */
	private String smsContent;

	/**
	 * 是否开启短信通知
	 */
	private Boolean smsEnabled;

	/**
	 * 短信发送手机号（多个用逗号隔开）
	 */
	private String smsPhone;

	/**
	 * 微信公众号模板
	 */
	private String wxTemplateId;

	/**
	 * 微信公众号内容
	 */
	private String wxContent;

	/**
	 * 是否开启微信公众号通知
	 */
	private Boolean wxEnabled;

	/**
	 * 微信公众号链接
	 */
	private String wxUrl;

	/**
	 * 站内信模板及内容
	 */
	private String systemMsgContent;

	/**
	 * 是否开启站内信通知
	 */
	private Boolean systemMsgEnabled;

}
