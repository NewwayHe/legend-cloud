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

import java.io.Serializable;
import java.util.Date;

/**
 * 订购历史DTO
 *
 * @author legendshop
 */
@Data
public class OrderHistoryDTO implements Serializable {

	/**
	 * 订购ID
	 */
	private Long orderId;

	/**
	 * 订单状态
	 */
	private String status;

	/**
	 * 修改时间
	 */
	private Date createTime;

	/**
	 * 操作的动作信息 如: xxoo 于 2015-05-21 15:40:23 提交订单
	 */
	private String reason;

}
