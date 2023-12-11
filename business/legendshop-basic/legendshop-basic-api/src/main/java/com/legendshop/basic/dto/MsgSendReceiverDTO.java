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
 * 群发消息状态(MsgStatus)实体类
 *
 * @author legendshop
 */
@Data

public class MsgSendReceiverDTO implements Serializable {

	private static final long serialVersionUID = 925626395968202028L;
	/**
	 * ID
	 */
	private Long id;


	/**
	 * 消息ID
	 */
	private Long msgId;


	/**
	 * 状态
	 */
	private Integer status;

}
