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
 * 商家订单结算订单
 *
 * @author legendshop
 */
@Data
@Schema(description = "商家订单结算订单")
public class ShopOrderBillOrderDTO implements Serializable {

	private static final long serialVersionUID = -2251647664322031168L;


	@Schema(description = "订单ID")
	private Long orderId;

	@Schema(description = "订单编号")
	private String orderNumber;


	@Schema(description = "下单时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date orderTime;

	@Schema(description = "支付时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date payTime;


	@Schema(description = "订单金额")
	private BigDecimal orderAmount;


	@Schema(description = "运费金额")
	private BigDecimal freightAmount;


	@Schema(description = "佣金金额")
	private BigDecimal commissionAmount;


	@Schema(description = "红包金额")
	private BigDecimal redpackAmount;

	@Schema(description = "退款金额")
	private BigDecimal refundAmount;

	@Schema(description = "抵扣金额")
	private BigDecimal totalDeductionAmount;

	@Schema(description = "使用积分")
	private BigDecimal totalIntegral;

	@Schema(description = "积分结算金额")
	private BigDecimal settlementPrice;

	@Schema(description = "兑换积分,比例 x:1")
	private BigDecimal proportion;
}
