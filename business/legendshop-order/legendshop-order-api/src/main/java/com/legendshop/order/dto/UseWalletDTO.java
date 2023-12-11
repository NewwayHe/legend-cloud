/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用于操作订单余额抵扣操作
 *
 * @author legendshop
 */
@Data
public class UseWalletDTO implements Serializable {

	@Schema(description = "预订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "预订单ID不能为空")
	private String confirmOrderId;

	@Schema(description = "是否开启标识", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotNull(message = "是否开启标识不能为空")
	private Boolean useWallet;

	@Schema(description = "钱包抵扣金额")
	private BigDecimal amount;

	@Schema(description = "支付密码")
	private String payPassword;
}
