/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 发起预支付入参
 *
 * @author legendshop
 */
@Data
@Schema(description = "发起预支付入参")
public class CreatePayDTO {

	@Schema(description = "用户ID")
	private Long userId;

	/**
	 * 支付单据类型[订单支付:ORDINARY_ORDER,预付款充值:USER_RECHARGE]
	 * {@link com.legendshop.pay.enums.SettlementTypeEnum}
	 */
	@Schema(description = "支付单据类型[订单支付:ORDINARY_ORDER,预付款充值:USER_RECHARGE,团购订单:GROUP_ORDER,秒杀订单:SEC_KILL_ORDER,拼团订单:MERGE_GROUP_ORDER]")
	@NotBlank(message = "支付单据类型不能为空")
	private String settlementType;


	/**
	 * 订单号或余额充值流水号等
	 */
	@Schema(description = "业务订单号集合[订单号或余额充值流水号等]")
	@NotEmpty(message = "业务订单号集合不能为空")
	private List<String> businessOrderNumberList;

}
