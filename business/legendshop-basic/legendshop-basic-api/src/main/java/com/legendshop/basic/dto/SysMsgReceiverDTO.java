/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 站内信接收者
 *
 * @author legendshop
 */
@Data
public class SysMsgReceiverDTO implements Serializable {

	/**
	 * 接收者ids
	 */
	private Long[] receiveUserIds;

	/**
	 * {@link com.legendshop.basic.enums.MsgReceiverTypeEnum}
	 */
	private Integer receiverType;
}
