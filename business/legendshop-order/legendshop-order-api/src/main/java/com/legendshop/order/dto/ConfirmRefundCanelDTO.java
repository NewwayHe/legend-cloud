/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import com.legendshop.common.core.annotation.EnumValid;
import com.legendshop.order.enums.OrderTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * 平台确认取消订单DTO
 *
 * @author legendshop
 */
@Data
public class ConfirmRefundCanelDTO {

	@Schema(description = "退款id")
	private Long refundId;

	@Schema(description = "平台备注")
	private String adminMessage;

	@NotNull(message = "订单类型不能为空")
	@Schema(description = "订单类型 O:普通订单  P:预售订单  G：团购订单  S：秒杀订单  MG：拼团订单 I:积分商品订单")
	@EnumValid(target = OrderTypeEnum.class)
	private String orderType;

	@NotNull(message = "是否同意不能为空")
	@Schema(description = "是否同意")
	private boolean agree;


	@Schema(description = "商家操作人")
	private String adminOperator;

	@Schema(description = "商家操作时间")
	private Date adminOperatorTime;
}
