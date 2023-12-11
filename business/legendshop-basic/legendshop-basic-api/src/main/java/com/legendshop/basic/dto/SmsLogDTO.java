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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信记录表(SmsLog)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "短信记录")
public class SmsLogDTO implements Serializable {


	private static final long serialVersionUID = 1248211773858033283L;

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	private Long userId;

	/**
	 * 手机号码
	 */
	@Schema(description = "手机号码")
	private String mobilePhone;


	/**
	 * 短信内容
	 */
	@Schema(description = "短信内容")
	private String content;


	/**
	 * 类型
	 * {@link MsgSendTypeEnum}
	 */
	@Schema(description = "短信内容")
	private String type;


	/**
	 * 发送时间
	 */
	@Schema(description = "发送时间")
	private Date createTime;


	/**
	 * 发送短信返回码
	 */
	@Schema(description = "发送短信返回码")
	private String responseStatus;


	/**
	 * 请求ip
	 */
	@Schema(description = "请求ip")
	private String requestIp;

	/**
	 * 发送渠道 阿里云 腾讯
	 * 参考SmsType
	 */
	@Schema(description = "发送渠道 阿里云 腾讯")
	private String channelType;

}
