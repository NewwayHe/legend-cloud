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
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author legendshop
 */
@Data
public class PaySettlementSuccessDTO implements Serializable {

	private static final long serialVersionUID = -4323748743316105734L;

	@Schema(description = "支付单号")
	private String settlementSn;

	@Schema(description = "支付金额")
	private BigDecimal amount;

	@Schema(description = "支付项")
	private List<PaySettlementOrderDTO> settlementItem;

	@Schema(description = "支付状态")
	private Integer state;

	@Schema(description = "订单id")
	private Long orderId;
}
