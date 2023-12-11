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
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.annotation.EnumValid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 消息推送生产dto
 *
 * @author legendshop
 */
@Data
public class MsgSendDTO implements Serializable {

	private static final long serialVersionUID = 7073535104965413081L;

	/**
	 * 站内信
	 * 类型 {@link MsgSendTypeEnum}
	 */
	@EnumValid(target = MsgSendTypeEnum.class)
	private MsgSendTypeEnum msgSendType;

	/**
	 * public
	 * 推送方式 {@link SysParamNameEnum}
	 */
	@EnumValid(target = SysParamNameEnum.class)
	private SysParamNameEnum sysParamNameEnum;


	/**
	 * 短信、微信模板替换的内容
	 * 短信、微信模板替换的内容
	 * 内容
	 * 注："dataDTOList":[
	 * {
	 * "dataName":"PRODUCT_NAME",
	 * "value":"iphone11"
	 * }
	 * ]
	 */
	@NotEmpty(message = "替换内容不能为空")
	private List<MsgSendParamDTO> msgSendParamDTOList;

	/**
	 * 跳转路径替换参数
	 */
	private List<MsgSendParamDTO> urlParamList;


	/**
	 * 详情id，例：商品通知传商品id,店铺通知传店铺id
	 */
	private Long detailId;


	/**
	 * 发送者
	 */
	private Long userId;


	/**
	 * 站内信接收者
	 */
	private List<SysMsgReceiverDTO> sysMsgReceiverDTOS;

	/**
	 * 接收者手机号列表
	 */
	private List<String> receiveUserPhoneNumbers;


	/**
	 * 接收者微信 openid
	 */
	private List<String> receiveUserWxOpenIds;


}
