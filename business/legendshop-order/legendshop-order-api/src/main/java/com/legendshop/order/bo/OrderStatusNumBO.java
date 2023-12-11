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
import lombok.Data;

import java.util.Optional;

/**
 * 订单状态数
 *
 * @author legendshop
 */
@Data
public class OrderStatusNumBO {

	@Schema(description = "待付款订单数")
	private Integer unpaidNum;

	@Schema(description = "待发货订单数")
	private Integer waitDeliveryNum;

	@Schema(description = "待签收订单数")
	private Integer consignmentNum;

	@Schema(description = "待收货订单数")
	private Integer takeDeliverNum;

	@Schema(description = "已完成订单数")
	private Integer success;

	@Schema(description = "已取消订单数")
	private Integer close;

	@Schema(description = "待评价数")
	private Integer unCommCount;

	@Schema(description = "售后订单处理中数")
	private Integer processingNum;

	@Schema(description = "售后完成数")
	private Integer refundSuccessNum;

	@Schema(description = "售后取消数")
	private Integer refundFailedNum;

	@Schema(description = "待开具发票数量")
	private Integer toBeInvoicedOrderCount;

	@Schema(description = "已开具发票数量")
	private Integer invoicedOrderCount;

	public Integer getUnpaidNum() {
		return Optional.ofNullable(unpaidNum).orElse(0);
	}

	public Integer getWaitDeliveryNum() {
		return Optional.ofNullable(waitDeliveryNum).orElse(0);
	}

	public Integer getConsignmentNum() {
		return Optional.ofNullable(consignmentNum).orElse(0);
	}

	public Integer getTakeDeliverNum() {
		return Optional.ofNullable(takeDeliverNum).orElse(0);
	}

	public Integer getSuccess() {
		return Optional.ofNullable(success).orElse(0);
	}

	public Integer getClose() {
		return Optional.ofNullable(close).orElse(0);
	}

	public Integer getUnCommCount() {
		return Optional.ofNullable(unCommCount).orElse(0);
	}

	public Integer getProcessingNum() {
		return Optional.ofNullable(processingNum).orElse(0);
	}

	public Integer getRefundSuccessNum() {
		return Optional.ofNullable(refundSuccessNum).orElse(0);
	}

	public Integer getRefundFailedNum() {
		return Optional.ofNullable(refundFailedNum).orElse(0);
	}
}
