/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
@Schema(description = "订单信息数量BO")
@Builder
public class OrderInfoCountsBO implements Serializable {

	private static final long serialVersionUID = 7421903970225895240L;

	/**
	 * 未发货订单数量
	 */
	@Schema(description = "未发货订单数量")
	private Integer waitDelivery;

	/**
	 * 未付款订单数量
	 */
	@Schema(description = "未付款订单数量")
	private Integer unPaid;

	/**
	 * 未收货订单数量
	 */
	@Schema(description = "未收货订单数量")
	private Integer takeDeliver;

	/**
	 * 未评论订单数量
	 */
	@Schema(description = "未评论订单数量")
	private Integer noComm;

	/**
	 * 未处理售后订单数量
	 */
	@Schema(description = "未处理售后订单数量")
	private Integer refundCount;
}
