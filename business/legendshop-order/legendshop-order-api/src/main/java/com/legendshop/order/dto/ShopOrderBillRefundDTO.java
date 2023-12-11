/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 账单退款订单
 *
 * @author legendshop
 */
@Data
@Schema(description = "账单退款订单")
public class ShopOrderBillRefundDTO implements Serializable {

	private static final long serialVersionUID = -5124767250532353695L;

	@Schema(description = "订单ID")
	private Long orderId;

	@Schema(description = "订单编号")
	private String orderNumber;

	@Schema(description = "下单时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date orderTime;

	@Schema(description = "售后编号")
	private String refundNumber;

	@Schema(description = "申请时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	@Schema(description = "退款金额")
	private BigDecimal refundAmount;
}
