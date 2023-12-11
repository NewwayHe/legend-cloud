/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.query;

import com.legendshop.order.enums.OrderRefundReturnStatusEnum;
import com.legendshop.order.enums.OrderStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 退款订单账单查询参数
 *
 * @author legendshop
 */
@Data
@Accessors(chain = true)
public class OrderRefundReturnBillQuery {

	/**
	 * 订单状态
	 */
	private Integer orderStatus;

	/**
	 * 退款处理状态
	 */
	private Integer handleSuccessStatus;

	/**
	 * 结算状态
	 */
	private Boolean billFlag;

	/**
	 * 申请状态
	 */
	private Integer applyStatus;

	/**
	 * 结束时间
	 */
	private Date endDate;

	/**
	 * 订单集合
	 */
	private List<Long> orderIds;

	public OrderRefundReturnBillQuery(Date endDate, List<Long> orderIds) {
		this.orderStatus = OrderStatusEnum.SUCCESS.getValue();
		this.handleSuccessStatus = OrderRefundReturnStatusEnum.HANDLE_SUCCESS.value();
		this.billFlag = Boolean.FALSE;
		this.applyStatus = OrderRefundReturnStatusEnum.APPLY_FINISH.value();
		this.endDate = endDate;
		this.orderIds = orderIds;
	}
}
