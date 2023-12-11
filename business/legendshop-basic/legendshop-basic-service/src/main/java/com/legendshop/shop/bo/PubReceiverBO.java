/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 群发消息状态(PubReceiver)BO
 *
 * @author legendshop
 * @since 2021-06-16 11:23:50
 */
@Data
public class PubReceiverBO implements Serializable {

	private static final long serialVersionUID = -57739558744474140L;

	/**
	 * ID
	 */
	@Schema(description = "ID")
	private Long id;

	/**
	 * 公告ID
	 */
	@Schema(description = "公告ID")
	private Integer pubId;

	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Integer userId;

	/**
	 * 状态，0未读，1已读
	 */
	@Schema(description = "状态，0未读，1已读")
	private Integer status;

	/**
	 * 1：普通用户 2：商家
	 */
	@Schema(description = "1：普通用户 2：商家 ")
	private Object type;

}
