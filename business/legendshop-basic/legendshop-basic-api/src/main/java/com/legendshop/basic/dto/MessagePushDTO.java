/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import com.legendshop.basic.enums.MsgSendTypeEnum;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.annotation.EnumValid;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 消息推送入参DTO
 *
 * @author legendshop
 */
@Data
@Accessors(chain = true)
public class MessagePushDTO implements Serializable {


	private static final long serialVersionUID = -2512681398108722863L;

	/**
	 * 接收者Id数组
	 */
	private Long[] receiveIdArr;

	/**
	 * 接收通知的角色类型
	 */
	@EnumValid(target = MsgReceiverTypeEnum.class)
	private MsgReceiverTypeEnum msgReceiverTypeEnum;


	/**
	 * 通知类型
	 */
	@EnumValid(target = MsgSendTypeEnum.class)
	private MsgSendTypeEnum msgSendType;


	/**
	 * 推送通知模板类型
	 */
	@EnumValid(target = SysParamNameEnum.class)
	private SysParamNameEnum sysParamNameEnum;


	/**
	 * 消息替换参数
	 */
	private List<MsgSendParamDTO> msgSendParamDTOList;

	/**
	 * 跳转路径参数
	 * http://xxxx?xx=xx&xx=xx
	 */
	private List<MsgSendParamDTO> urlParamList;

	/**
	 * 详情id，例：商品通知传商品id,店铺通知传店铺id
	 */
	private Long detailId;

	/**
	 * 接收者手机号列表
	 */
	private List<String> receiveUserPhoneNumbers;


	/**
	 * 接收者微信 openid
	 */
	private List<String> receiveUserWxOpenIds;
}
