/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import lombok.Data;

/**
 * 订单留言
 *
 * @author legendshop
 */
@Data
public class OrderMessageDTO {

	/**
	 * 店铺ID
	 */
	private Long shopId;

	/**
	 * 买家留言
	 */
	private String message;
}
